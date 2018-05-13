package cn.edu.nju.software.common.pojo.bizservice;

import cn.edu.nju.software.common.pojo.AddressInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel
 * @since 2018/5/13 0:24
 */
@Data
public class UIBatchItemAdd {
    AddressInfo addressInfo;
    String batchNum;
    List<String> itemIds;
    int itemTypeId;

}
