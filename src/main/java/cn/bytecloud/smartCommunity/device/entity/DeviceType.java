package cn.bytecloud.smartCommunity.device.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum DeviceType implements BaseEnum{
    BALL(1,"球机"),
    GUN(2,"枪机");

    private Integer type;
    private String value;
    DeviceType(int type,String value) {
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
