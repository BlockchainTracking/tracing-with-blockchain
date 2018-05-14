package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.dao.DealerDao;
import cn.edu.nju.software.ui.temp.dao.DealerItemDao;
import cn.edu.nju.software.ui.temp.dao.DealerItemTypeDao;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.entity.DealerItem;
import cn.edu.nju.software.ui.temp.entity.DealerItemType;
import cn.edu.nju.software.ui.temp.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:18 AM.
 * Illustration:
 */
@Service
public class DealServiceImpl implements DealerService{
    
    
    private final DealerItemDao dealerItemDao;
    
    private final DealerItemTypeDao dealerItemTypeDao;
    
    @Autowired
    public DealServiceImpl(DealerItemDao dealerItemDao, DealerItemTypeDao dealerItemTypeDao) {
        this.dealerItemDao = dealerItemDao;
        this.dealerItemTypeDao = dealerItemTypeDao;
    }
    
    @Override
    public BizResponse<List<DealerItemType>> getAllDealerItemTypes() {
        
        return BizResponse.deafaultResponse(dealerItemTypeDao.findAll());
    }
    
    @Override
    public BizResponse<List<DealerItem>> getAllDealerItems(int organizationId) {
        
        return BizResponse.deafaultResponse(dealerItemDao.findAllByDealerId(organizationId));
    }
    
    
    @Override
    public BizResponse sellItem(int organizationId , String itemIdString ) {
        
        Arrays.asList(itemIdString.split(",")).forEach(itemId -> dealerItemDao.deleteByDealerIdAndItemId(organizationId, itemId));
        
        return BizResponse.deafaultResponse(null);
        
    }
    
    @Override
    public BizResponse addItem(int organizationId, String itemIdString , int dealerItemTypeId) {
    
        
        List<DealerItem> dealerItemList = Arrays.stream(itemIdString.split(","))
                                          .map(itemId -> new DealerItem(organizationId , itemId , dealerItemTypeId))
                                          .collect(Collectors.toList());
    
        dealerItemDao.saveAll(dealerItemList);
        return BizResponse.deafaultResponse(null);
        
    }
}
