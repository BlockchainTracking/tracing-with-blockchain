package cn.edu.nju.software.fabricservice.appinvoke;

import cn.edu.nju.software.common.pojo.fabricservice.FSResponse;
import cn.edu.nju.software.common.util.LoggerUtil;
import cn.edu.nju.software.fabricservice.HFClientHelper;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel
 * @since 2018/4/28 16:54
 */
public class ChaincodeInvoker {
    Logger logger = LoggerFactory.getLogger(ChaincodeInvoker.class);

    private static final Map<String, ChaincodeInvokerConfig> ALL_CHAINCODES;

    static {
        ALL_CHAINCODES = new HashMap<>();

        ALL_CHAINCODES.put(ChaincodeInvokerId.ITEM_ADD.getId(),
                new ChaincodeInvokerConfig(
                        InvokeType.INVOKE,
                        Requests.ItemAddRequest.class,
                        null,
                        null));

        ALL_CHAINCODES.put(ChaincodeInvokerId.ITEM_CHANGE.getId(),
                new ChaincodeInvokerConfig(
                        InvokeType.INVOKE,
                        Requests.ItemChangeRequest.class,
                        null,
                        null));

        ALL_CHAINCODES.put(ChaincodeInvokerId.ITEM_GET.getId(),
                new ChaincodeInvokerConfig(
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

    public FSResponse<Object> invoke(ChaincodeInvokerId chaincodeInvokerId,
                                     AbstractMessageLite request,
                                     InvokeParameter invokeParameter) {
        ChaincodeInvokerConfig config = ALL_CHAINCODES.get(chaincodeInvokerId.getId());
        if (config.getRequestType() != null && !config.getRequestType().isInstance(request)) {
            return FSResponse.createWithoutData(-1, "ERROR request type, except:%s, actual:%s",
                    config.getRequestType().getName(), request.getClass().getName());
        }
        Collection<ProposalResponse> responses;
        switch (config.getInvokeType()) {
            case QUERY:
                responses = HFClientHelper.chainCodeQuery(chaincodeInvokerId.getName(),
                        chaincodeInvokerId.getVersion(), chaincodeInvokerId.getFunc(),
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
                responses = HFClientHelper.chainCodeInvoke(chaincodeInvokerId.getName(),
                        chaincodeInvokerId.getVersion(), chaincodeInvokerId.getFunc(),
                        request.toByteArray());
                if (responses == null) {
                    return FSResponse.createWithoutData(-1, "error invoke chaincode:%s",
                            chaincodeInvokerId.getId());
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
                        HFClientHelper.sendTransactions(success);
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
    private static class ChaincodeInvokerConfig {
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
        ChaincodeInvoker chaincodeInvoker = new ChaincodeInvoker();
        FSResponse<Object> re = chaincodeInvoker.invoke(ChaincodeInvokerId.ITEM_GET,
                Requests.ItemGetRequest.newBuilder().setItemId
                        ("12345678901234567890123456789012")
                        .setHistData(true).build(),
                null);
        System.out.println(new Gson().toJson(re.respStatus));
        System.out.println(new Gson().toJson(re.getData()));
    }
}
