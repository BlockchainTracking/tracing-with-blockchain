package cn.edu.nju.software.ui.temp.dao;

import cn.edu.nju.software.ui.temp.entity.LogisticsTerminal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:10 PM.
 * Illustration:
 */
public interface LogisticsTerminalDao extends JpaRepository<LogisticsTerminal , Integer> {
    
    LogisticsTerminal findByToken(String token);
}
