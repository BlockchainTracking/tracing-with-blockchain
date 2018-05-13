package cn.edu.nju.software.ui.temp.entity;

import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:15 PM.
 * Illustration:
 */
@Entity
@Table(name = "organization")
public class Organization {
    
    @Id
    private int id;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "token")
    private String token;
    
    @JoinTable(name = "organization_and_item" , joinColumns = @JoinColumn(name = "organization_id"))
    @Column(name = "item_id")
    @ElementCollection(targetClass = String.class)
    private List<String> itemIds;
}
