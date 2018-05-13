package cn.edu.nju.software.ui.temp.dao;

import cn.edu.nju.software.ui.temp.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:23 PM.
 * Illustration:
 */
public interface OrganizationDao extends JpaRepository<Organization , Integer>{
    
    Organization findByToken(String token);
}
