package cn.edu.nju.software.common.pojo.fabricservice;

import cn.edu.nju.software.common.pojo.RespStatus;

/**
 * @author Daniel
 * @since 2018/4/28 15:39
 */
public class FSResponse<T> {
    public RespStatus respStatus;
    public T data;
}
