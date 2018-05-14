package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.dao.DealerDao;
import cn.edu.nju.software.ui.temp.dao.LogisticsSiteDao;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.entity.LogisticsSite;
import cn.edu.nju.software.ui.temp.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:18 AM.
 * Illustration:
 */
@Service
public class LogisticsServiceImpl implements LogisticsService{
    
    @Autowired
    private LogisticsSiteDao logisticsSiteDao;
    
    @Autowired
    private DealerDao dealerDao;
    
    @Override
    public BizResponse<List<LogisticsSite>> getAllLogisticsSite(int organizationId) {
        List<LogisticsSite> list = logisticsSiteDao.findAll().stream()
                                   .filter(site -> site.getId()!=organizationId)
                                   .collect(Collectors.toList());
        return BizResponse.deafaultResponse(list);
    }
    
    
    @Override
    public BizResponse addOrder(String orderNum, String destination, int dealerId, String itemIdString, String description, String email) {
        return null;
    }
    
    @Override
    public BizResponse addPath(String orderNum, int fromId, int toId, Date date) {
        return null;
    }
    
    @Override
    public BizResponse signOrder(String orderNum, Date date) {
        return null;
    }
    
    @Override
    public BizResponse<List<String>> getAllOrderNumsInsite(int organizationId) {
        return null;
    }
}
