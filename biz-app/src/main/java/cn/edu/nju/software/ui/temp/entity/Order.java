package cn.edu.nju.software.ui.temp.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:45 PM.
 * Illustration:
 */
public class Order {
    
    @Id
    private int orderId;
    
    @Column(name = "item_id")
    private String itemId;
    
    @Column(name = "email")
    private String email;
    
    @OneToMany(targetEntity =  Path.class)
    @JoinColumn(name = "order_id")
    private List<Path> paths;
}
