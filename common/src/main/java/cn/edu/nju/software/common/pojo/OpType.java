package cn.edu.nju.software.common.pojo;

public enum OpType {
    //出厂
    CREATED,
    //出售给分销商/代理商
    TRANSFER,
    //中转
    MEDIA,
    //入库
    INSTOCK,
    //出库
    OUTSTOCK,
    //送达
    DELIVERED;


    public static OpType getOpTypeByIndex(int index) {
        OpType[] types = OpType.values();
        if (index >= types.length)
            return null;
        return types[index];
    }

}

