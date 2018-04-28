package cn.edu.nju.software.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应状态
 */
@AllArgsConstructor
@NoArgsConstructor
public class RespStatus {
    /**
     * 状态码
     */
    @Getter
    @Setter
    private Integer code;
    /**
     * 状态描述
     */
    @Getter
    @Setter
    private String msg;
}
