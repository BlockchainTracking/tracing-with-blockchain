package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.bean.request.LogisticsArrivedRequest;
import cn.edu.nju.software.ui.bean.request.LogisticsDepartureRequest;
import cn.edu.nju.software.ui.bean.request.LogisticsOrderRequest;
import cn.edu.nju.software.ui.bean.request.LogisticsSignRequest;
import cn.edu.nju.software.ui.bean.response.LogisSiteResponse;
import cn.edu.nju.software.ui.bean.response.ToArriveOrder;
import cn.edu.nju.software.ui.bean.response.ToDepartureOrder;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Daniel
 * @since 2018/5/13 0:19
 */
@RestController
@RequestMapping("/logis")
public class LogisticsController {
    @Autowired
    ItemTypeDao itemTypeDao;

    @RequestMapping(value = "/itemDeparture", method = RequestMethod.POST)
    @ApiOperation(value = "商品出发，站点库存减少订单")
    public BizResponse carDeparture(
            @ApiParam
            @RequestBody LogisticsDepartureRequest departureRequest) {
        return null;
    }

    @RequestMapping(value = "/itemArrived", method = RequestMethod.POST)
    @ApiOperation(value = "商品到达，站点库存增加订单")
    public BizResponse carArrived(
            @ApiParam
            @RequestBody LogisticsArrivedRequest arrivedRequest) {
        return null;
    }

    @RequestMapping(value = "/toDepartureOrder", method = RequestMethod.POST)
    @ApiOperation(value = "获得可以出发的订单")
    public BizResponse<List<ToDepartureOrder>> getToDepartureOrders() {
        return null;
    }

    @RequestMapping(value = "/toArriveOrder", method = RequestMethod.POST)
    @ApiOperation(value = "获得可以到达的订单")
    public BizResponse<List<ToArriveOrder>> getToArriveOrders() {
        return null;
    }

    @RequestMapping(value = "/toDepartureNode", method = RequestMethod.POST)
    @ApiOperation(value = "获得所有可以出发的节点")
    public BizResponse<List<LogisSiteResponse>> getAllDepartureStations() {
        return null;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ApiOperation(value = "物流订单")
    public BizResponse logisOrder(
            @ApiParam
            @RequestBody LogisticsOrderRequest orderRequest) {
        return null;
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ApiOperation(value = "商品签收，订单完成，如果为分销商订单则分销商的库存需要增加")
    public BizResponse orderSign(
            @ApiParam
            @RequestBody LogisticsSignRequest logisticsSignRequest) {
        return null;
    }


}
