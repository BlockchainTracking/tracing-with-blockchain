package cn.edu.nju.software.common.pojo;

/**
 * @author Daniel
 * @since 2018/4/28 15:40
 */
public class TracingItemInfo {
    /**
     * 时间戳
     */
    long timestamp;
    /**
     * 商品信息
     */
    ItemInfo itemInfo;
    /**
     * 商品状态
     */
    ItemState itemState;
    /**
     * 环境状态
     */
    EnvStatus envStatus;
    /**
     * 操作状态
     */
    OperationStatus operationStatus;

}
