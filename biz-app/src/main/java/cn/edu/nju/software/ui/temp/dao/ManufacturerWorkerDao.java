package cn.edu.nju.software.ui.temp.dao;

import cn.edu.nju.software.ui.temp.entity.ManufacturerWorker;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:12 PM.
 * Illustration:
 */
public interface ManufacturerWorkerDao extends JpaRepository<ManufacturerWorker , Integer>{
    
    ManufacturerWorker findByAccountAndPassword(String account , String password);
}
