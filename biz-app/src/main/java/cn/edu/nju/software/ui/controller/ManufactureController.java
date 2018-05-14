package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.ItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.bean.SessionKey;
import cn.edu.nju.software.ui.bean.request.BatchAddRequest;
import cn.edu.nju.software.ui.bean.request.ManufactureOrderRequest;
import cn.edu.nju.software.ui.bean.response.RecallResponse;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import cn.edu.nju.software.ui.temp.entity.ItemType;
import cn.edu.nju.software.ui.temp.entity.User;
import cn.edu.nju.software.ui.temp.service.ManufacturerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Daniel
 * @since 2018/5/13 0:19
 */
@RestController
@RequestMapping("/manuf")
public class ManufactureController {
    @Autowired
    ItemTypeDao itemTypeDao;

    @Autowired
    ManufacturerService manufacturerService;

    @RequestMapping(value = "/addItems", method = RequestMethod.POST)
    @ApiOperation(value = "商品批次出厂")
    public BizResponse addItems(
            @ApiParam
            @RequestBody BatchAddRequest uiBatchAddReqeust, HttpSession session) {
        User user = (User) session.getAttribute(SessionKey.USR);

//        manufacturerService.addBatch()

//        ItemTypeEntity itemTypeEntity = itemTypeDao.findById(uiBatchItemAdd.getItemTypeId()).get();

        return null;
    }


    @RequestMapping(value = "/deleteType", method = RequestMethod.POST)
    @ApiOperation(value = "删除商品类型")
    public BizResponse deleteItemTypes(int typeId) {
        try {
            itemTypeDao.deleteById(typeId);
        } catch (Exception e) {
            return BizResponse.createWithoutData(-1, "delete error");
        }
        return BizResponse.createSuccess(null, "success");

    }

    @RequestMapping(value = "/addItemTypes", method = RequestMethod.POST)
    @ApiOperation(value = "增加商品类型")
    public BizResponse addItemTypes(
            String typeName,
            String typeClass) {
        ItemType itemTypeEntity = new ItemType();
        itemTypeEntity.setItemName(typeName);
        itemTypeEntity.setItemClass(typeClass);
        System.out.println(typeName);
        itemTypeDao.save(itemTypeEntity);
        return BizResponse.createSuccess(itemTypeEntity, "success");
    }

    @RequestMapping(value = "/allBrokers", method = RequestMethod.POST)
    @ApiOperation(value = "获得所有的经销商")
    public BizResponse<List<String>> allBrokers() {
        return null;
    }


    @RequestMapping(value = "/allItemTypes", method = RequestMethod.POST)
    @ApiOperation(value = "获得所有的商品类型")
    public BizResponse<List<ItemType>> getAllItemTypes() {
        return BizResponse.createSuccess(itemTypeDao.findAll(), "success");
    }

    @RequestMapping(value = "/itemGet", method = RequestMethod.POST)
    @ApiOperation(value = "获得商品信息,所有或单个")
    public BizResponse itemGet(String batchNum) {
        return null;
    }

    @RequestMapping(value = "/recall", method = RequestMethod.POST)
    @ApiOperation(value = "商品召回")
    public BizResponse<RecallResponse> itemRecall(String batchNum) {
        return null;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ApiOperation(value = "商品订单")
    public BizResponse<RecallResponse> manufactureOrder(
            @RequestBody ManufactureOrderRequest orderRequest) {
        return null;
    }


}
