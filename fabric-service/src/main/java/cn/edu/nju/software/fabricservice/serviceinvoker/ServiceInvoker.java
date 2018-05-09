package cn.edu.nju.software.fabricservice.serviceinvoker;

import cn.edu.nju.software.common.pojo.fabricservice.FSResponse;
import cn.edu.nju.software.common.util.LoggerUtil;
import cn.edu.nju.software.fabricservice.configmgt.ConfigMgt;
import cn.edu.nju.software.fabricservice.configmgt.ConfigType;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
import cn.edu.nju.software.fabricservice.servicediscovery.DiscoveryParas;
import cn.edu.nju.software.fabricservice.servicediscovery.LoadBalanceType;
import cn.edu.nju.software.fabricservice.servicediscovery.ServiceDiscovery;
import com.google.gson.Gson;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel
 * @since 2018/4/28 16:54
 */
public class ServiceInvoker {
    Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);

    private ConfigMgt configMgt;
    private HFClientHelper hfClientHelper;
    private ServiceDiscovery serviceDiscovery;

    private static final Map<String, ServiceInvokerConfig> ALL_SERVICES;

    static {
        ALL_SERVICES = new HashMap<>();

        ALL_SERVICES.put(ServiceInvokerId.ITEM_ADD.getId(),
                new ServiceInvokerConfig(
                        InvokeType.INVOKE,
                        Requests.ItemAddRequest.class,
                        null,
                        null));

        ALL_SERVICES.put(ServiceInvokerId.ITEM_CHANGE.getId(),
                new ServiceInvokerConfig(
                        InvokeType.INVOKE,
                        Requests.ItemChangeRequest.class,
                        null,
                        null));

        ALL_SERVICES.put(ServiceInvokerId.ITEM_GET.getId(),
                new ServiceInvokerConfig(
                        InvokeType.QUERY,
                        Requests.ItemGetRequest.class,
                        ResponseOuterClass.ItemGetResponse.class,
                        e -> {
                            try {
                                return ResponseOuterClass.ItemGetResponse.parseFrom(e);
                            } catch (InvalidProtocolBufferException e1) {
                                e1.printStackTrace();
                            }
                            return null;
                        }));
    }

    public ServiceInvoker(InvokerInitParameters invokerInitParameters) {
        //默认为文件类型
        if (invokerInitParameters == null || invokerInitParameters.getConfigType() == ConfigType.FILE) {
            Object[] paras = invokerInitParameters == null ? null : invokerInitParameters
                    .getConfigParas();

            configMgt = new ConfigMgt();
            hfClientHelper = new HFClientHelper();
            serviceDiscovery = new ServiceDiscovery();

            configMgt.init(ConfigType.FILE, paras);
            hfClientHelper.init(configMgt.getConfig());
            serviceDiscovery.init(configMgt.getConfig());
        }
    }

    public ServiceInvoker() {
        this(null);
    }


    void configInvokeContext(ServiceInvokerId serviceInvokerId) {
        DiscoveryParas discoveryParas = new DiscoveryParas();
        discoveryParas.setPeerNum(1);
        discoveryParas.setLoadBalanceType(LoadBalanceType.POLLING);
        InvokeContext invokeContext = serviceDiscovery.findService(serviceInvokerId, discoveryParas);
        hfClientHelper.setContext(invokeContext);
    }

    public FSResponse<Object> invoke(ServiceInvokerId serviceInvokerId,
                                     AbstractMessageLite request,
                                     InvokeParameter invokeParameter) {

        //配置执行上下文
        configInvokeContext(serviceInvokerId);

        ServiceInvokerConfig config = ALL_SERVICES.get(serviceInvokerId.getId());
        if (config.getRequestType() != null && !config.getRequestType().isInstance(request)) {
            return FSResponse.createWithoutData(-1, "ERROR request type, except:%s, actual:%s",
                    config.getRequestType().getName(), request.getClass().getName());
        }
        Collection<ProposalResponse> responses;
        switch (config.getInvokeType()) {
            case QUERY:
                responses = hfClientHelper.chainCodeQuery(serviceInvokerId.getName(),
                        serviceInvokerId.getVersion(), serviceInvokerId.getFunc(),
                        request.toByteArray());
                for (ProposalResponse response : responses) {
                    if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
                        Object obj = null;
                        try {
                            if (config.getRequestType() != null || response
                                    .getChaincodeActionResponsePayload() != null) {
                                obj = config.getParseFunction().apply(response.getChaincodeActionResponsePayload());
                            }
                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                        return FSResponse.createSuccess(obj, "query success");
                    }
                }
                return FSResponse.createWithoutData(-1, "ERROR querry, no peer return success " +
                        "result");
            case INVOKE:
                responses = hfClientHelper.chainCodeInvoke(serviceInvokerId.getName(),
                        serviceInvokerId.getVersion(), serviceInvokerId.getFunc(),
                        request.toByteArray());
                if (responses == null) {
                    return FSResponse.createWithoutData(-1, "error invoke chaincode:%s",
                            serviceInvokerId.getId());
                }
                Collection<ProposalResponse> success = new ArrayList<>();
                for (ProposalResponse response : responses) {
                    if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
                        success.add(response);
                    } else {
                        LoggerUtil.errorf(logger, "error invoke :%s", response.getMessage());
                    }
                }
                try {
                    Collection<Set<ProposalResponse>> re = SDKUtils.getProposalConsistencySets(responses);
                    if (re.size() != 1) {
                        //TODO peer状态不一致，自行决定需不需要发送到客户端
                    } else {
                        hfClientHelper.sendTransactions(success);
                        return FSResponse.createSuccess(null, "invoke success");
                    }
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
                break;
        }
        return FSResponse.createWithoutData(-1, "error invoke");
    }


    /**
     * 确定发起调用的是哪个chaincode的哪个函数
     */
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ServiceInvokerConfig {
        @Setter
        @Getter
        InvokeType invokeType;
        @Setter
        @Getter
        Class<?> requestType;
        @Setter
        @Getter
        Class<?> respType;
        @Setter
        @Getter
        Function<byte[], Object> parseFunction;

    }


    public static void main(String[] args) {
        ServiceInvoker serviceInvoker = new ServiceInvoker(null);
        FSResponse<Object> re = serviceInvoker.invoke(ServiceInvokerId.ITEM_GET,
                Requests.ItemGetRequest.newBuilder().setItemId
                        ("12345678901234567890123456789012")
                        .setHistData(true).build(),
                null);
        System.out.println(new Gson().toJson(re.respStatus));
        System.out.println(new Gson().toJson(re.getData()));
    }
}