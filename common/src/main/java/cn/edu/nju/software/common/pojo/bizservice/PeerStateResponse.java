package cn.edu.nju.software.common.pojo.bizservice;

import lombok.Data;

/**
 * @author Daniel
 * @since 2018/5/12 8:41
 */
@Data
public class PeerStateResponse {
    String peerName;
    String peerUrl;
    int currentInvoke;
    int totalInvoke;
    boolean state;
}
