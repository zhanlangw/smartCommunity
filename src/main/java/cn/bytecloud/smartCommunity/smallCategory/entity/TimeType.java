package cn.bytecloud.smartCommunity.smallCategory.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum TimeType implements BaseEnum{
    DAY(1,"天"),HOUR(2,"小时");
    private Integer type;
    private String value;

    TimeType(int type, final String value) {
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
