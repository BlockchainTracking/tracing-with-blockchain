package cn.edu.nju.software.ui;

import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
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
     * @param itemID 商品的唯一ID
     * @return
     */
    BizResponse<List<TracingItemInfo>> getTracingItemInfo(String itemID);

    /**
     * 获得商品最新的信息
     *
     * @param itemID 商品的唯一ID
     * @return
     */
    BizResponse<TracingItemInfo> getCurrentItemInfo(String itemID);

    /**
     * 增加一条商品溯源信息
     *
     * @param tracingItemInfo
     * @return
     */
    BizResponse addItemTracingInfo(TracingItemInfo tracingItemInfo);



}
