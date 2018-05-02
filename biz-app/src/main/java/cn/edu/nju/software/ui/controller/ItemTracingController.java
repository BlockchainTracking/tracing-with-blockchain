package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.UIItemAddRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemChangeRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemGetRequest;
import cn.edu.nju.software.ui.bizservice.ItemTracingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Daniel
 * @since 2018/5/2 15:21
 */
@RestController
@Api("溯源信息api")
public class ItemTracingController {

    @Autowired
    ItemTracingService itemTracingService;

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    @ApiOperation(value = "获得当前的商品状态信息")
    public BizResponse<List<TracingItemInfo>> getTracingItemInfo(
            @ApiParam(value = "获得商品信息", required = true)
            @RequestBody UIItemGetRequest request) {
        return itemTracingService.getTracingItemInfo(request);
    }


    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    @ApiOperation(value = "增加一件商品")
    public BizResponse getTracingItemInfo(
            @ApiParam(value = "增加的商品的信息", required = true)
            @RequestBody UIItemAddRequest request) {
        return itemTracingService.addItemTracingInfo(request);
    }

    @RequestMapping(value = "/item/change", method = RequestMethod.POST)
    @ApiOperation(value = "改变一件商品信息")
    public BizResponse changeTracingItemInfo(
            @ApiParam(value = "改变商品的信息,opType取值为0,1,2分别代表创建、物流、送达", required = true)
            @RequestBody UIItemChangeRequest request) {
        return itemTracingService.changeItemTracingInfo(request);
    }


}
