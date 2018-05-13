package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Daniel
 * @since 2018/5/13 0:19
 */
@RestController
@RequestMapping("/logis")
public class LogisticsController {
    @Autowired
    ItemTypeDao itemTypeDao;

    @RequestMapping(value = "/carPackaging", method = RequestMethod.POST)
    @ApiOperation(value = "商品装车")
    public BizResponse carPacking() {
        return null;
    }

    @RequestMapping(value = "/carArrived", method = RequestMethod.POST)
    @ApiOperation(value = "商品到达")
    public BizResponse carArrived() {
        return null;
    }

    @RequestMapping(value = "/instock", method = RequestMethod.POST)
    @ApiOperation(value = "商品入库")
    public BizResponse inStock() {
        return null;
    }

    @RequestMapping(value = "/outstock", method = RequestMethod.POST)
    @ApiOperation(value = "商品出库")
    public BizResponse outStock() {
        return null;
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ApiOperation(value = "商品签收")
    public BizResponse itemSign() {
        return null;
    }


}
