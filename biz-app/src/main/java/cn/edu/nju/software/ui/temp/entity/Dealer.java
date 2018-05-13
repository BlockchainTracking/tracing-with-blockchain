package cn.edu.nju.software.ui.temp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.util.List;

/**
 * Author:yangsanyang
 * Time:2018/5/13 5:15 PM.
 * Illustration:
 */
@Entity
@Table(name = "dealer")
@Data
@NoArgsConstructor
public class Dealer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "address")
    private String address;
    
    
    @JoinTable(name = "dealer_and_item" , joinColumns = @JoinColumn(name = "dealer_id"))
    @Column(name = "item_id")
    @ElementCollection(targetClass = String.class)
    private List<String> itemIds;
}
