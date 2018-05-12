package cn.edu.nju.software.common.pojo;

import lombok.Data;

/**
 * @author Daniel
 * @since 2018/4/28 16:43
 * 商品状态，表示商品当前的状态，可以通过内部传感器采集
 */
@Data
public class ItemStatus {
    boolean normal;
    String logs;
}
