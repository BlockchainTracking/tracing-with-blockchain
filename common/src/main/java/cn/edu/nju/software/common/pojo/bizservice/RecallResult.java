package cn.edu.nju.software.common.pojo.bizservice;

import cn.edu.nju.software.common.pojo.AddressInfo;
import lombok.Data;

/**
 * @author Daniel
 * @since 2018/5/13 0:59
 */
@Data
public class RecallResult {
    AddressInfo currentAddress;
    String contact;
}
