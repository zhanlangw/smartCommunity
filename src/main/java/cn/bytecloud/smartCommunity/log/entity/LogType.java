package cn.bytecloud.smartCommunity.log.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum LogType implements BaseEnum {
    CREATE(1,"创建案卷"),END(2,"结束"),ORDINATY(3,"正常"),WITHDRAW(4,"撤回"),WITHDRAW_END(5,"撤回结束"),APIINSPECTION(6,"AI巡查");
    private Integer type;
    private String value;

    LogType(int type, final String value) {
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
