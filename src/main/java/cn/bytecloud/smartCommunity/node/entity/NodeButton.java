package cn.bytecloud.smartCommunity.node.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum NodeButton implements BaseEnum {
    SUBMIT(1,"提交"),WITHDRAW(2,"撤回"),SPECIAL_DELIVERY(3,"特送"),END(4,"结束"),ACCEPT(5,"受理"),DELETE(6,"删除");

    private Integer type;
    private String value;

    NodeButton(int type, final String value) {
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
