package cn.edu.nju.software.fabricservice.appinvoke;

/**
 * 发起调用的返回类型
 */
public enum ReturnType {
    /**
     * 直接返回，丢弃结果，不对结果做任何处理，不管任何错误情况
     */
    DIRECTLY,
    /**
     * 同步调用，等待结果返回
     */
    SYNC,
    /**
     * 异步调用，需要设置回调函数
     */
    ASYNC
}
