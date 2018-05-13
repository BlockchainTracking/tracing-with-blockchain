package cn.edu.nju.software.ui.bean.request;

import lombok.Data;

/**
 * @author Daniel
 * @since 2018/5/13 23:23
 */
@Data
public class LogisticsOrderRequest {
    String orderDesc;
    String departureAddress;
    String items;
}
