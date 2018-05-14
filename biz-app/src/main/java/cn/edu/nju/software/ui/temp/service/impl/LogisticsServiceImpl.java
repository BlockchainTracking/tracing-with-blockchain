package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.entity.LogisticsSite;
import cn.edu.nju.software.ui.temp.service.LogisticsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:18 AM.
 * Illustration:
 */
@Service
public class LogisticsServiceImpl implements LogisticsService{
    @Override
    public BizResponse<List<LogisticsSite>> getAllLogisticsSite(int organizationId) {
        return null;
    }
    
    @Override
    public BizResponse<List<Dealer>> getAllDealers() {
        return null;
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
