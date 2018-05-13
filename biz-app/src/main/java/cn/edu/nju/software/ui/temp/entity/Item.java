package cn.edu.nju.software.ui.temp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:12 PM.
 * Illustration:
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
public class Item {
    
    @Id
    private int id;
    
    @Column(name = "item_id")
    private String itemId;
    
    @Column(name = "batch_num")
    private String batchNum;
    
    public Item(String itemId, String batchNum) {
        this.itemId = itemId;
        this.batchNum = batchNum;
    }
}
