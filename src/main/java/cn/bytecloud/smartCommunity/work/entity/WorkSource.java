package cn.bytecloud.smartCommunity.work.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum WorkSource implements BaseEnum{
    //案件来源
    APP(1,"手机APP上报"),SYSTEM(2,"摄像机抓拍"),WEB(3,"管理员分配"),AIINSPECTION(4,"AI巡查");

    private Integer type;
    private String value;

    WorkSource(int type, final String value) {
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
