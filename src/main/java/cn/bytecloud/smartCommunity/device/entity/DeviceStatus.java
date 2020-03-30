package cn.bytecloud.smartCommunity.device.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum DeviceStatus implements BaseEnum{
    ONLINE(1,"在线"),LESS(2,"丢失");

    private Integer type;

    private String value;
    DeviceStatus(Integer type,String value) {
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
