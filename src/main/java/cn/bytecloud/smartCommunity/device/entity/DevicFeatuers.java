package cn.bytecloud.smartCommunity.device.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum DevicFeatuers implements BaseEnum{
    INTELLIGENT(1,"智能"),
    ORDINARY(2,"普通");

    private Integer type;
    private String value;
    DevicFeatuers(int type,String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Integer getEnumType() {
        return type;
    }

    @Override
    public String getEnumValue() {
        return value;
    }
}
