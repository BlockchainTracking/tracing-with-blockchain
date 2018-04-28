package cn.edu.nju.software.common.pojo.bizservice;

import cn.edu.nju.software.common.pojo.RespStatus;

/**
 * 响应
 *
 * @param <T>
 */
public class BizResponse<T> {
    /**
     * 响应状态码
     */
    public RespStatus respStatus;
    /**
     * 响应数据
     */
    public T respData;
}
