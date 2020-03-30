package cn.bytecloud.smartCommunity.path.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

/**
 * 路径属性
 *
 * @author WIN10
 */
public enum PathAttribute implements BaseEnum {
    //
    DELAY(1, "申请延时"), TODO(2, "待处置"), RETURN(3, "退件"), REVIEW(4, "至管理员待审核"), ADDUSER(5, "新增办理人"), AGREE(6, "同意"), REFUSE(7, "拒绝"), END(8, "结束"), WITHDRAW(9, "撤回"),
    SPECIAL_DELIVERY(10, "特送"), CREATE(11, "新建"), RETURN_REFUSE(12, "退件拒绝"), APIINSPECTION(13, "AI巡查"),;
    private Integer type;
    private String value;

    PathAttribute(int type, final String value) {
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
