package cn.edu.nju.software.ui.temp.service;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.ui.temp.entity.Dealer;

import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/14 12:04 AM.
 * Illustration:
 */
public interface DealerService {
    

    
    /**
     * 获取所有的库存信息
     * @return List<itemId>
     */
    BizResponse<List<String>> getAllItemIds();
    
    /**
     * 销售item
     * @param itemIdString itemId组成的String，以","分隔
     * @return 操作结果
     */
    BizResponse sellItem(String itemIdString);
    

}
