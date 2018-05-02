package cn.edu.nju.software.common.pojo;

public enum OpType {
    CREATED,
    LOGISTICS,
    DELIVERED;


    public static OpType getOpTypeByIndex(int index) {
        OpType[] types = OpType.values();
        if (index >= types.length)
            return null;
        return types[index];
    }
}

