package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.ItemInfo;
import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.request.UIItemGetRequest;
import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.bean.SessionKey;
import cn.edu.nju.software.ui.bean.request.BatchAddRequest;
import cn.edu.nju.software.ui.bean.request.ManufactureOrderRequest;
import cn.edu.nju.software.ui.bean.response.RecallItem;
import cn.edu.nju.software.ui.bean.response.RecallResponse;
import cn.edu.nju.software.ui.bean.response.StockInfoResponse;
import cn.edu.nju.software.ui.bizservice.ItemTracingService;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import cn.edu.nju.software.ui.temp.entity.Dealer;
import cn.edu.nju.software.ui.temp.entity.ItemType;
import cn.edu.nju.software.ui.temp.entity.User;
import cn.edu.nju.software.ui.temp.service.ManufacturerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    ItemTracingService itemTracingService;

    @RequestMapping(value = "/addItems", method = RequestMethod.POST)
    @ApiOperation(value = "商品批次出厂")
    public BizResponse addItems(
            @ApiParam
            @RequestBody BatchAddRequest uiBatchAddReqeust, HttpSession session) {
        User user = (User) session.getAttribute(SessionKey.USR);

        return manufacturerService.addBatch(user.getOrganizationId(), uiBatchAddReqeust
                        .getBatchNum(),
                uiBatchAddReqeust.getItems(), uiBatchAddReqeust.getItemTypeId());
//        manufacturerService.addBatch()

//        ItemTypeEntity itemTypeEntity = itemTypeDao.findById(uiBatchItemAdd.getItemTypeId()).get();
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
    public BizResponse<List<Dealer>> allBrokers() {
        return manufacturerService.getAllDealers();
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
        BizResponse<List<String>> certainBatchItemIds = manufacturerService.getCertainBatchItemIds(batchNum);
        List<TracingItemInfo> tracingItemInfos = new ArrayList<>();
        for (String str : certainBatchItemIds.getRespData()) {
            UIItemGetRequest uiItemGetRequest = new UIItemGetRequest();
            uiItemGetRequest.setAllItem(false);
            uiItemGetRequest.setHistData(false);
            uiItemGetRequest.setItemId(str);
            BizResponse<List<TracingItemInfo>> tracingItemInfo = itemTracingService.getTracingItemInfo(uiItemGetRequest);
            if (tracingItemInfo.getRespStatus().isSuccess()) {
                tracingItemInfos.addAll(tracingItemInfo.getRespData());
            }
        }
        BizResponse<ItemType> batch = manufacturerService.getBatch(batchNum);
        RecallResponse recallResponse = new RecallResponse();
        recallResponse.setItemType(batch.getRespData());
        List<RecallItem> collect = tracingItemInfos.stream().map(e -> {
            RecallItem recallItem = new RecallItem();
            recallItem.setItemId(e.getItemId());
            recallItem.setItemAddress(e.getEnvStatus().getAddressInfo().getAddressDesc());
            recallItem.setContact(e.getOperationStatus().getContactWay());
            return recallItem;
        }).collect(Collectors.toList());
        recallResponse.setRecallItems(collect);
        return BizResponse.createSuccess(recallResponse, "successs");
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ApiOperation(value = "商品订单")
    public BizResponse manufactureOrder(
            @RequestBody ManufactureOrderRequest orderRequest, HttpSession session) {
        User user = (User) session.getAttribute(SessionKey.USR);
        return manufacturerService.addSellingOrder(user.getOrganizationId(), orderRequest
                        .getOrgId(),
                orderRequest.getDepartureAddress(), orderRequest.getItems(), "111@hh.com");
    }

    @RequestMapping(value = "/stock", method = RequestMethod.POST)
    @ApiOperation(value = "商品库存")
    public BizResponse<List<StockInfoResponse>> stockInfos(HttpSession session) {
        User user = (User) session.getAttribute(SessionKey.USR);
        BizResponse<Map<ItemType, Integer>> resp = manufacturerService.getUnsoldItemTypeAndNumber
                (user.getOrganizationId());
        List<StockInfoResponse> stockInfoResponses = new ArrayList<>();
        if (!resp.getRespStatus().isSuccess())
            return BizResponse.createWithoutData(-1, resp.respStatus.getMsg());
        resp.getRespData().forEach((k, v) -> {
            StockInfoResponse infoResponse = new StockInfoResponse();
            infoResponse.setItemType(k.getItemName());
            infoResponse.setCount(v);
            stockInfoResponses.add(infoResponse);
        });
        return BizResponse.createSuccess(stockInfoResponses, "success");

    }


}
