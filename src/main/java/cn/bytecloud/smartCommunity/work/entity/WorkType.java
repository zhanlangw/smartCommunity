package cn.bytecloud.smartCommunity.work.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum WorkType implements BaseEnum {
    URGENT(1,"紧急"),ORDINARY(2,"普通");
    private Integer type;
    private String value;

    WorkType(int type, final String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getEnumValue() {
        return value;
    }

    @Override
    public Integer getEnumType() {
        return type;
    }
}
