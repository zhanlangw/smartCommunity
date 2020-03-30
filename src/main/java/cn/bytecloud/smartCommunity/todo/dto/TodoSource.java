package cn.bytecloud.smartCommunity.todo.dto;

import cn.bytecloud.smartCommunity.util.BaseEnum;

/**
 * 代办来源
 */
public enum TodoSource implements BaseEnum{
    SUBMIT(1,"提交"),WITHDRAW(2,"撤回"),SPECIAL_DELIVERY(3,"特送"),END(4,"结束");

    private Integer type;
    private String value;

    TodoSource(int type, final String value) {
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
