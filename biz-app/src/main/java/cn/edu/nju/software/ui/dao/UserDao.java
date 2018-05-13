package cn.edu.nju.software.ui.dao;

import cn.edu.nju.software.ui.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDao extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);
}
