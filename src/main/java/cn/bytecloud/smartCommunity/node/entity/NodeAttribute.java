package cn.bytecloud.smartCommunity.node.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum NodeAttribute implements BaseEnum {
    //环节主次
    MASTER(1, "主"), SLAVE(2, "次");
    private Integer type;
    private String value;

    NodeAttribute(int type, final String value) {
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
