package cn.edu.nju.software.ui.temp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:22 PM.
 * Illustration:
 */
@Entity
@Table(name = "manufacturer")
public class Manufacturer {
    
    @Id
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    public Manufacturer(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
