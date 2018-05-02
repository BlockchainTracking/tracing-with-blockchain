package cn.edu.nju.software.fabricservice.appinvoke;

import cn.edu.nju.software.fabricservice.config.HFConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Daniel
 * @since 2018/5/2 12:19
 */
@AllArgsConstructor
@NoArgsConstructor
public class ChaincodeInvokerId {
    private static final String CC_NAME = HFConfig.newInstance().getCcName();
    private static final String CC_VERSION = HFConfig.newInstance().getCcVersion();
    //TODO 这里的"myCC需要改"
    public static final ChaincodeInvokerId ITEM_ADD = new ChaincodeInvokerId(CC_NAME,
            CC_VERSION, "addItem");

    public static final ChaincodeInvokerId ITEM_GET = new ChaincodeInvokerId(CC_NAME,
            CC_VERSION, "getItem");

    public static final ChaincodeInvokerId ITEM_CHANGE = new ChaincodeInvokerId(CC_NAME,
            CC_VERSION, "changeItem");
    @Setter
    @Getter
    String name;
    @Setter
    @Getter
    String version;
    @Setter
    @Getter
    String func;

    public String getId() {
        return name + ":" + version + "-" + func;
    }
}