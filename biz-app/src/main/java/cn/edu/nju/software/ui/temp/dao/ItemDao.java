package cn.edu.nju.software.ui.temp.dao;

import cn.edu.nju.software.ui.temp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:00 PM.
 * Illustration:
 */
public interface ItemDao extends JpaRepository<Item , Integer>{
    
    Item findByItemId(String itemId);
}
