package cn.edu.nju.software.fabricservice.serviceinvoker;

/**
 * @author Daniel
 * @since 2018/5/2 12:19
 */

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.BlockEvent;

import java.util.function.Consumer;

/**
 * 调用参数，用来控制调用过程，如是否异步等
 */
@AllArgsConstructor
@NoArgsConstructor
public class InvokeParameter {
    /**
     * 返回类型
     */
    private ReturnType returnType;
    /**
     * 回调函数，尽在returenType为ASYNC、queryType为QUERY时有用
     */
    private Consumer<Object> querycallBack;

    private Consumer<BlockEvent.TransactionEvent> invokeCallBack;


}
