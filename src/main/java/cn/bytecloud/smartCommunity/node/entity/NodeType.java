package cn.bytecloud.smartCommunity.node.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

/**
 * 环节类型
 */
public enum NodeType implements BaseEnum {
    START(1,"开始环节"),ORDINARY(2,"普通环节"),END(3,"结束环节");

    private Integer type;
    private String value;

    NodeType(int type, final String value) {
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
