package cn.edu.nju.software.ui.bizservice;

import cn.edu.nju.software.common.pojo.EnvStatus;
import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.UIItemAddRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemChangeRequest;
import cn.edu.nju.software.common.pojo.bizservice.UIItemGetRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品溯源信息接口
 */
@Service
public interface ItemTracingService {

    /**
     * 获得商品的所有溯源信息
     *
     * @param uiItemGetRequest 商品的唯一ID
     * @return
     */
    BizResponse<List<TracingItemInfo>> getTracingItemInfo(UIItemGetRequest uiItemGetRequest);


    /**
     * 增加一条商品溯源信息
     *
     * @param uiItemAddRequest
     * @return
     */
    BizResponse addItemTracingInfo(UIItemAddRequest uiItemAddRequest);

    /**
     * 改变一个商品的状态
     *
     * @param uiItemChangeRequest
     * @return
     */
    BizResponse changeItemTracingInfo(UIItemChangeRequest uiItemChangeRequest);


}
