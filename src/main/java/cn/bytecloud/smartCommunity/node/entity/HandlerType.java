package cn.bytecloud.smartCommunity.node.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum HandlerType implements BaseEnum {
    WORKER(1,"默认工作人员"),ROOT(2,"默认管理员"), CUSTOMIZE(3, "自定义");

    private Integer type;
    private String value;

    HandlerType(int type, final String value) {
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
