package cn.edu.nju.software.ui.dao;

import cn.edu.nju.software.ui.entity.ItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTypeDao extends JpaRepository<ItemTypeEntity, Integer> {
}
