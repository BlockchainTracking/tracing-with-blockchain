package cn.edu.nju.software.ui.temp.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:46 PM.
 * Illustration:
 */
public class Path {
    
    @Id
    private int id;
    
    @Column(name = "from")
    private String from;
    
    @Column(name = "to")
    private String to;
    
    @Column(name = "date")
    private Date date;
    
    @Column(name = "orderState")
    private OrderState orderState;
    
    
}
