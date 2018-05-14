package cn.edu.nju.software.ui.temp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:45 PM.
 * Illustration:
 */
@Entity
@Table(name = "logistics_order")
@Data
@NoArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "order_num")
    private String orderNum;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "destination")
    private String destination;
    
    @JoinTable(name = "order_and_item" , joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item_id")
    @ElementCollection(targetClass = String.class)
    private List<String> itemIds = new ArrayList<>();
    
    @Column(name = "dealer_id")
    private int dealerId;
    
    @Column(name = "email")
    private String email;
    
    @OneToMany(targetEntity =  Path.class)
    @JoinColumn(name = "order_id")
    private List<Path> paths = new ArrayList<>();
    
    @Column(name = "orderState")
    private OrderState orderState;
    
    public Order(String orderNum, String description, List<String> itemIds, String email, List<Path> paths) {
        this.orderNum = orderNum;
        this.description = description;
        this.itemIds = itemIds;
        this.email = email;
        this.paths = paths;
    }
}
