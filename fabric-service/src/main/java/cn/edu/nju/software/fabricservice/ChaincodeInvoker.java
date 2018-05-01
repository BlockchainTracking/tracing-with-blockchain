package cn.edu.nju.software.fabricservice;

import cn.edu.nju.software.common.pojo.fabricservice.FSRequest;
import cn.edu.nju.software.common.pojo.fabricservice.FSResponse;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
import com.google.protobuf.GeneratedMessageV3;
import org.hyperledger.fabric.sdk.HFClient;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Daniel
 * @since 2018/4/28 16:54
 */
public class ChaincodeInvoker {
    HFClient hfClient;

    public FSResponse<Object> invoke(String chaincodeIdAndFunc,Object request) {
        //TODO 待添加
        return null;
    }

    /**
     * 确定发起调用的是哪个chaincode的哪个函数
     */
    static class ChaincodeInvokerConfig {
        String name;
        String func;
        Class<?> requestType;
        Function<?, ResponseOuterClass.Response> function;

    }
}
