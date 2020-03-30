package cn.bytecloud.smartCommunity.todo.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum TodoType implements BaseEnum{
    //
    TODO(1,"待处置"),REVIEW(2,"待核查"),DELAY(3,"申请延时"),RETURN(4,"退件"),ACCEPT(5,"待受理");

    private Integer type;
    private String value;

    TodoType(int type, final String value) {
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
