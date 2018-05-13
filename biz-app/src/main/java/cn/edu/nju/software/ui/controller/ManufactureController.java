package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.RecallResult;
import cn.edu.nju.software.common.pojo.bizservice.UIBatchItemAdd;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import cn.edu.nju.software.ui.temp.entity.ItemType;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/manuf")
public class ManufactureController {
    @Autowired
    ItemTypeDao itemTypeDao;

    @RequestMapping(value = "/addItems", method = RequestMethod.POST)
    @ApiOperation(value = "商品批次出厂")
    public BizResponse addItems(
            @RequestBody UIBatchItemAdd uiBatchItemAdd) {
//        ItemTypeEntity itemTypeEntity = itemTypeDao.findById(uiBatchItemAdd.getItemTypeId()).get();

        for (String itemId : uiBatchItemAdd.getItemIds()) {
//            Requests.ItemAddRequest itemAddRequest =
        }
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
        itemTypeDao.save(itemTypeEntity);
        return BizResponse.createSuccess(itemTypeEntity, "success");
    }


    @RequestMapping(value = "/allItemTypes", method = RequestMethod.POST)
    @ApiOperation(value = "获得所有的商品类型")
    public BizResponse<List<ItemType>> getAllItemTypes() {
        return BizResponse.createSuccess(itemTypeDao.findAll(), "success");
    }

    @RequestMapping(value = "/recall", method = RequestMethod.POST)
    @ApiOperation(value = "商品召回")
    public BizResponse<List<RecallResult>> itemRecall(String batchNum) {
        return null;
    }





}
