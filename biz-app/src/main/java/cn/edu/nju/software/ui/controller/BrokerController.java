package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.RecallResult;
import cn.edu.nju.software.common.pojo.bizservice.UIBatchItemAdd;
import cn.edu.nju.software.ui.dao.ItemTypeDao;
import cn.edu.nju.software.ui.entity.ItemTypeEntity;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/broker")
public class BrokerController {

    @RequestMapping(value = "/inStock", method = RequestMethod.POST)
    @ApiOperation(value = "商品入库")
    public BizResponse addItems(
            @RequestBody UIBatchItemAdd uiBatchItemAdd) {
        return null;
    }

    @RequestMapping(value = "/itemOrder", method = RequestMethod.POST)
    @ApiOperation(value = "商品订单")
    public BizResponse itemOrder(
            @RequestBody UIBatchItemAdd uiBatchItemAdd) {
        return null;
    }


}
