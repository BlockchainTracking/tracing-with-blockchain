package cn.edu.nju.software.ui.temp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:40 PM.
 * Illustration:
 */
@Entity
@Table(name = "logistics_site")
@Data
@NoArgsConstructor
public class LogisticsSite {
    
    @Id
    private int id;
    
    @Column(name = "address")
    private String address;
    
    public LogisticsSite(String address) {
        this.address = address;
    }
}
