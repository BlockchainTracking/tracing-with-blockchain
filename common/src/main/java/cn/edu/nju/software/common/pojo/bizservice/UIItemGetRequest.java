package cn.edu.nju.software.common.pojo.bizservice;

import lombok.Data;

/**
 * @author Daniel
 * @since 2018/5/2 16:41
 */
@Data
public class UIItemGetRequest {
    String authCode;
    String itemId;
    boolean histData;
}
