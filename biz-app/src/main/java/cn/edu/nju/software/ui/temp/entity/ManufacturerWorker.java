package cn.edu.nju.software.ui.temp.entity;

import javax.persistence.*;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:25 PM.
 * Illustration:
 */
@Entity
@Table(name = "manufacturer_worker")
public class ManufacturerWorker {
    
    @Id
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "sex")
    private String sex;
    
    @Column(name = "account")
    private String account;
    
    @Column(name = "password")
    private String password;
    
    @ManyToOne(targetEntity = Manufacturer.class)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
}
