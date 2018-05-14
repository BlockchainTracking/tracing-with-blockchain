package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.dao.DealerDao;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.service.DealerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:18 AM.
 * Illustration:
 */
@Service
public class DealServiceImpl implements DealerService{
    
    private DealerDao dealerDao;
    
    @Override
    public BizResponse<List<String>> getAllItemIds(int organizationId) {
        
        List<String> list = dealerDao.getOne(organizationId).getItemIds();
        return BizResponse.deafaultResponse(list);
    }
    
    @Override
    public BizResponse sellItem(int organizationId , String itemIdString) {
        
        Dealer dealer = dealerDao.getOne(organizationId);
        
        dealer.getItemIds().removeAll(Arrays.asList(itemIdString.split(",")));
        dealerDao.save(dealer);
        
        return BizResponse.deafaultResponse(null);
        
    }
    
    @Override
    public BizResponse addItem(int organizationId, String itemIdString) {
    
        Dealer dealer = dealerDao.getOne(organizationId);
    
        dealer.getItemIds().addAll(Arrays.asList(itemIdString.split(",")));
        dealerDao.save(dealer);
    
        return BizResponse.deafaultResponse(null);
        
    }
}
