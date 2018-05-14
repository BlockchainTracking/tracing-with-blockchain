package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.temp.entity.ItemType;
import cn.edu.nju.software.ui.temp.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:19 AM.
 * Illustration:
 */
@Service
public class ManufacturerServiceImpl implements ManufacturerService{
    @Override
    public BizResponse<List<ItemType>> getAllItemTypes() {
        return null;
    }
    
    @Override
    public BizResponse addBatch(int organizationId, String itemIdString, int itemTypeId, Date date) {
        return null;
    }
    
    @Override
    public BizResponse<List<String>> getAllItemIds(int organizationId) {
        return null;
    }
    
    @Override
    public BizResponse<List<String>> getCertainBatchItemIds(int organizationId) {
        return null;
    }
    
    @Override
    public BizResponse<List<String>> getAllBatchNums(int organizationId) {
        return null;
    }
    
    @Override
    public BizResponse<Map<String, List<String>>> getBatchAndItemIds(int organizationId) {
        return null;
    }
    
    @Override
    public BizResponse<Map<String, String>> getItemIdAndEmail(int organizationId, String batchNum) {
        return null;
    }
    
    @Override
    public BizResponse<ItemType> getBatch(String batchNum) {
        return null;
    }
}
