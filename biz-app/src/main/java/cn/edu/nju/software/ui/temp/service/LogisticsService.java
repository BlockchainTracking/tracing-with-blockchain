package cn.edu.nju.software.ui.temp.service;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.entity.LogisticsSite;

import java.util.Date;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/14 12:03 AM.
 * Illustration:
 */
public interface LogisticsService {
    
    /**
     * 获取所有的物流站点信息
     * @param organizationId 需要去除该站点的id
     * @return List<LogisticsSite>
     */
    BizResponse<List<LogisticsSite>> getAllLogisticsSite(int organizationId);
    

    
    /**
     * 添加一条订单
     * @param orderNum 订单号
     * @param destination 目的地
     * @param itemIdString itemId组成的String,用","分割
     * @param description 订单的描述
     * @return 添加结果
     */
    BizResponse addOrder(String orderNum ,String destination  , String itemIdString , String description );
    
    /**
     * 添加一条路径
     * @param orderNum 订单号
     * @param fromId 出发的站点号
     * @param toId 下一站的站点号,0代表订单正在派送中
     * @param date 时间
     * @return 操作结果
     */
    BizResponse addPath(String orderNum , int fromId , int toId , Date date);
    
    /**
     * 签收订单
     * @param orderNum 订单号
     * @param date 日期
     * @return 操作结果
     */
    BizResponse signOrder(String orderNum , Date date );
    
    /**
     * 获得该站点内所有的订单编号
     * @param organizationId 站点id
     * @return List<orderId>
     */
    BizResponse<List<String>> getAllOrderNumsInsite(int organizationId);
}
