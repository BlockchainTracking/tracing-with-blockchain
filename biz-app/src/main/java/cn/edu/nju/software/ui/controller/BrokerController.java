package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/broker")
public class BrokerController {

    @RequestMapping(value = "/allItem", method = RequestMethod.POST)
    @ApiOperation(value = "获得库存中所有的商品")
    public BizResponse<List<String>> allItems() {
        return null;
    }

    @RequestMapping(value = "/inStock", method = RequestMethod.POST)
    @ApiOperation(value = "商品入库")
    public BizResponse addItems() {
        return null;
    }

    @RequestMapping(value = "/itemOrder", method = RequestMethod.POST)
    @ApiOperation(value = "商品订单，减库存，然后运输")
    public BizResponse itemOrder() {
        return null;
    }


}
