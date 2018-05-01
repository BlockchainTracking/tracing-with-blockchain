package cn.edu.nju.software.fabricservice;

import cn.edu.nju.software.common.pojo.RespStatus;
import cn.edu.nju.software.common.pojo.fabricservice.FSRequest;
import cn.edu.nju.software.common.pojo.fabricservice.FSResponse;
import cn.edu.nju.software.common.util.LoggerUtil;
import cn.edu.nju.software.fabricservice.protomsg.Persistence;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
import com.google.gson.Gson;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.net.httpserver.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel
 * @since 2018/4/28 16:54
 */
public class ChaincodeInvoker {
    Logger logger = LoggerFactory.getLogger(ChaincodeInvoker.class);

    public static final Map<String, ChaincodeInvokerConfig> ALL_CHAINCODES;

    static {
        ALL_CHAINCODES = new HashMap<>();

        ALL_CHAINCODES.put(ChaincodeInvokerId.ITEM_ADD.getId(),
                new ChaincodeInvokerConfig(
                        InvokeType.INVOKE,
                        Requests.ItemAddRequest.class,
                        null,
                        null));

        ALL_CHAINCODES.put(ChaincodeInvokerId.ITEM_GET.getId(),
                new ChaincodeInvokerConfig(
                        InvokeType.QUERY,
                        Requests.ItemGetRequest.class,
                        Persistence.ItemAsset.class,
                        e -> {
                            try {
                                return Persistence.ItemAsset.parseFrom(e);
                            } catch (InvalidProtocolBufferException e1) {
                                e1.printStackTrace();
                            }
                            return null;
                        }));
    }


    public ChaincodeInvoker() {

    }

    public FSResponse<Object> invoke(ChaincodeInvokerId chaincodeInvokerId,
                                     AbstractMessageLite request,
                                     InvokeParameter invokeParameter) {
        ChaincodeInvokerConfig config = ALL_CHAINCODES.get(chaincodeInvokerId.getId());
        if (config.getRequestType() != null && !config.getRequestType().isInstance(request)) {
            return FSResponse.createWithoutData(-1, "ERROR requsttype, except:%s, actual:%s",
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
                        ResponseOuterClass.Response response1 = parseResponse(response);
                        if (response1 != null && response1.getCode() == RespStatus.SUCCESS_CODE) {
                            Object obj = null;
                            if (config.getRequestType() != null) {
                                obj = config.getParseFunction().apply(response1.getData());
                            }
                            return FSResponse.createSuccess(obj, "query success");
                        } else {
                            return FSResponse.createWithoutData(-1, "query error:",
                                    response1.getMessage());
                        }
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

    private ResponseOuterClass.Response parseResponse(ProposalResponse proposalResponse) {
        try {
            return ResponseOuterClass.Response.parseFrom(proposalResponse.getChaincodeActionResponsePayload());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用参数，用来控制调用过程，如是否异步等
     */
    static class InvokeParameter {

    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class ChaincodeInvokerId {
        //TODO 这里的"myCC需要改"
        public static final ChaincodeInvokerId ITEM_ADD = new ChaincodeInvokerId("myCC1",
                "1.0", "addItem");

        public static final ChaincodeInvokerId ITEM_GET = new ChaincodeInvokerId("myCC1",
                "1.0", "getItem");

        @Setter
        @Getter
        String name;
        @Setter
        @Getter
        String version;
        @Setter
        @Getter
        String func;

        public String getId() {
            return name + ":" + version + "-" + func;
        }
    }

    /**
     * 确定发起调用的是哪个chaincode的哪个函数
     */
    @AllArgsConstructor
    @NoArgsConstructor
    static class ChaincodeInvokerConfig {
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
        Function<ByteString, Object> parseFunction;

    }

    enum InvokeType {
        INVOKE, QUERY
    }


    public static void main(String[] args) {
        ChaincodeInvoker chaincodeInvoker = new ChaincodeInvoker();
        FSResponse<Object> re = chaincodeInvoker.invoke(ChaincodeInvokerId.ITEM_GET,
                Requests.ItemGetRequest.newBuilder().setItemId
                        ("1234567890123456789012345678901212345678901234567890123456789012")
                        .setHistData(false).build(),
                null);
        System.out.println(new Gson().toJson(re.respStatus));
        System.out.println(((Persistence.ItemAsset) re.getData()).toString());
    }
}
