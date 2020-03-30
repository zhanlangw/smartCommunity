package cn.bytecloud.smartCommunity.record.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum RecordType implements BaseEnum {
    //记录类型
    ORDINARY(1, "普通"), DELAY(2, "申请延时"), END(3, "结束"),RETURN(4,"退件");

    private Integer type;
    private String value;

    RecordType(int type, final String value) {
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
