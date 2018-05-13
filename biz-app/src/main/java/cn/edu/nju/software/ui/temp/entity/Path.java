package cn.edu.nju.software.ui.temp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Author:yangsanyang
 * Time:2018/5/13 4:46 PM.
 * Illustration:
 */
@Entity
@Table(name = "logistics_path")
@Data
@NoArgsConstructor
public class Path {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "from_location")
    private String from;
    
    @Column(name = "to_location")
    private String to;
    
    @Column(name = "date")
    private Date date;
    
    
    
}
