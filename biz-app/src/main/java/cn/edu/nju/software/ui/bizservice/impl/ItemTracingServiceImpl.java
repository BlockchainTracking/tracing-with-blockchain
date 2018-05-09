package cn.edu.nju.software.ui.bizservice.impl;

import cn.edu.nju.software.common.pojo.*;
import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.UIItemAddRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemChangeRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemGetRequest;
import cn.edu.nju.software.common.pojo.fabricservice.FSResponse;
import cn.edu.nju.software.common.util.DateUtil;
import cn.edu.nju.software.fabricservice.serviceinvoker.ServiceInvoker;
import cn.edu.nju.software.fabricservice.serviceinvoker.ServiceInvokerId;
import cn.edu.nju.software.fabricservice.serviceinvoker.requestbuilder.RequestsBuilder;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
import cn.edu.nju.software.ui.bizservice.ItemTracingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel
 * @since 2018/5/2 14:15
 */
@Service
public class ItemTracingServiceImpl implements ItemTracingService {
    @Autowired
    ServiceInvoker serviceInvoker;

    @Override
    public BizResponse<List<TracingItemInfo>> getTracingItemInfo(UIItemGetRequest uiItemGetRequest) {
        Requests.ItemGetRequest getRequest = RequestsBuilder.newItemGetRequestsBuilder()
                .setItemId(uiItemGetRequest.getItemId())
                .setHistData(uiItemGetRequest.isHistData()).build();
        FSResponse<Object> resp = serviceInvoker.invoke(ServiceInvokerId.ITEM_GET,
                getRequest, null);
        if (!RespStatus.SUCCESS_CODE.equals(resp.respStatus.getCode()))
            return BizResponse.createWithoutData(-1, "error invoke with message:%s", resp
                    .getRespStatus().getMsg());
        ResponseOuterClass.ItemGetResponse itemAsset = (ResponseOuterClass.ItemGetResponse) resp.getData();
        if (itemAsset.getItemAssetsList() == null) {
            return BizResponse.createWithoutData(-1, "null result");
        }
        List<TracingItemInfo> tracingItemInfos = itemAsset.getItemAssetsList().stream().map
                (BizConverter::convertTracingItemInfo).collect(Collectors.toList());

        return BizResponse.createSuccess(tracingItemInfos, "success");
    }


    @Override
    public BizResponse addItemTracingInfo(UIItemAddRequest uiItemAddRequest) {
        ItemInfo item = uiItemAddRequest.getItemInfo();
        AddressInfo addressInfo = uiItemAddRequest.getAddressInfo();
        Requests.ItemAddRequest itemAddRequest = RequestsBuilder.newItemAddRequestBuilder()
                .setItemId(uiItemAddRequest.getItemId())
                .setItemName(item.getName())
                .setBatchNum(item.getBatchNum())
                .setClass_(item.getClass_())
                .setManufactureDate(DateUtil.parseDate(item.getManufactureDate()).getTime())
                .setNote(item.getNote())
                .setAddressName(addressInfo.getNodeName())
                .setLongitude(addressInfo.getLongtitude())
                .setLatitude(addressInfo.getLatitude())
                .build();
        FSResponse response = serviceInvoker.invoke(ServiceInvokerId.ITEM_ADD, itemAddRequest,
                null);
        if (!response.getRespStatus().isSuccess()) {
            return BizResponse.createWithoutData(-1, response.getRespStatus().getMsg());
        }
        return BizResponse.createSuccess(null, "success");
    }

    @Override
    public BizResponse changeItemTracingInfo(UIItemChangeRequest uiItemChangeRequest) {
        ItemStatus itemStatus = uiItemChangeRequest.getItemStatus();
        EnvStatus envStatus = uiItemChangeRequest.getEnvStatus();
        AddressInfo addressInfo = envStatus.getAddressInfo();
        Requests.ItemChangeRequest itemChangeRequest = RequestsBuilder.newItemChangeRequestsBuilder()
                .setItemId(uiItemChangeRequest.getItemId())
                .setAddressName(addressInfo.getNodeName())
                .setLongitude(addressInfo.getLongtitude())
                .setLatitude(addressInfo.getLatitude())
                .setSampleLong(itemStatus.getSampleLong())
                .setSampleStr(itemStatus.getSampleStr())
                .setLogs(itemStatus.getLogs())
                .setOpType(OpType.getOpTypeByIndex(uiItemChangeRequest.getOpType()))
                .build();
        FSResponse response = serviceInvoker.invoke(ServiceInvokerId.ITEM_CHANGE, itemChangeRequest,
                null);
        if (!response.getRespStatus().isSuccess()) {
            return BizResponse.createWithoutData(-1, response.getRespStatus().getMsg());
        }
        return BizResponse.createSuccess(null, "success");
    }
}
