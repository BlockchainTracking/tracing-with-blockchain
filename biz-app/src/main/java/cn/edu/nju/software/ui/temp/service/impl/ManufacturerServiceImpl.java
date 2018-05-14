package cn.edu.nju.software.ui.temp.service.impl;

import cn.edu.nju.software.common.pojo.bizservice.response.BizResponse;
import cn.edu.nju.software.ui.bean.ManufactureOrderType;
import cn.edu.nju.software.ui.temp.dao.BatchDao;
import cn.edu.nju.software.ui.temp.dao.ItemDao;
import cn.edu.nju.software.ui.temp.dao.ItemTypeDao;
import cn.edu.nju.software.ui.temp.entity.*;
import cn.edu.nju.software.ui.temp.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author:yangsanyang
 * Time:2018/5/14 9:19 AM.
 * Illustration:
 */
@Service
public class ManufacturerServiceImpl implements ManufacturerService{
    
    @Autowired
    private ItemTypeDao itemTypeDao;
    
    @Autowired
    private ItemDao itemDao;
    
    @Autowired
    private BatchDao batchDao;
    
    @Override
    public BizResponse<List<ItemType>> getAllItemTypes() {
        
        return BizResponse.deafaultResponse(itemTypeDao.findAll());
        
    }
    
    @Override
    public BizResponse addBatch(int organizationId,String batchNum ,  String itemIdString, int itemTypeId) {
    
        Batch batch = new Batch(batchNum , new Date() , itemTypeId , organizationId);
        batchDao.save(batch);
        
        List<Item> itemList = Stream.of(itemIdString.split(",")).map(itemId -> new Item(itemId , batchNum)).collect(Collectors.toList());
        itemDao.saveAll(itemList);
        
        return BizResponse.deafaultResponse(null);
    }
    
    @Override
    public BizResponse<List<String>> getAllSoldItemIds(int organizationId) {
        
        List<String> itemIdList = new ArrayList<>();
        
        batchDao.findAllByManufacturerId(organizationId).stream().map(Batch::getBatchNum).forEach(batchNum -> {
            itemDao.findAllByBatchNum(batchNum).stream().filter(item -> !item.getItemStatus().equals(ItemStatus.unsold)).forEach(item -> itemIdList.add(item.getItemId()));
        });
        return BizResponse.deafaultResponse(itemIdList);
    }
    
    @Override
    public BizResponse<Map<ItemType, Integer>> getUnsoldItemTypeAndNumber(int organizationId) {
        
        List<ItemType> itemTypeList = itemTypeDao.findAll();
        Map<Integer , ItemType> map = new HashMap<>();
        itemTypeList.forEach(itemType -> map.put(itemType.getId() , itemType));
        
        List<Batch> batchList = batchDao.findAllByManufacturerId(organizationId);
        Map<Batch , Long> map1 = new HashMap<>();
        batchList.forEach(batch -> map1.put(batch , itemDao.findAllByBatchNum(batch.getBatchNum()).stream().filter(item -> item.getItemStatus().equals(ItemStatus.unsold)).count()));
        
        Map<Integer , Integer> map2 = new HashMap<>();
        map1.keySet().forEach(batch -> {
            int itemTypeId = batch.getItemTypeId();
            int number = map1.get(batch).intValue();
            if (map2.containsKey(itemTypeId))
                map2.put(itemTypeId , map2.get(itemTypeId)+number);
            else
                map2.put(itemTypeId , number);
        });
        
        Map<ItemType,Integer> map3 = new HashMap<>();
        map.keySet().forEach(id -> {
            map3.put(map.get(id), map2.getOrDefault(id, 0));
        });
        
        return BizResponse.deafaultResponse(map3);
        
        
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
    
    @Override
    public BizResponse<List<Dealer>> getAllDealers() {
        return null;
    }
    
    @Override
    public BizResponse addSellingOrder(int organizationId, int dealerId, String destination, String itemIdString) {
        return null;
    }
}
