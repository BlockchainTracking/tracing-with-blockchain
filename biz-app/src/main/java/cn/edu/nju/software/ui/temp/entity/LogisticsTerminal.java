package cn.edu.nju.software.ui.temp.entity;

import javax.persistence.*;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:43 PM.
 * Illustration:
 */
@Entity
@Table(name = "logistics_terminal")
public class LogisticsTerminal {
    
    @Id
    private int id;
    
    @Column(name = "token")
    private String token;
    
    @ManyToOne(targetEntity = LogisticsSite.class)
    @JoinColumn(name = "logistics_site_id")
    private LogisticsSite logisticsSite;
    
}
