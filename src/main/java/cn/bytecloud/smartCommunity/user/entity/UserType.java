package cn.bytecloud.smartCommunity.user.entity;

import cn.bytecloud.smartCommunity.util.BaseEnum;

public enum UserType implements BaseEnum {
    //
    ROOT(1, "系统管理员"), ADMIN(2, "平台管理员"), USER(3, "智能部门人员"), WORKER(4, "工作人员"), SUPERVISION_USER(5, "督查人员");

    private Integer type;
    private String value;

    UserType(int type, final String value) {
        this.type = type;
        this.value = value;
    }

    public static UserType getValue(String name) {
        switch (name) {
            case "平台管理员":
                return ADMIN;
            case "智能部门人员":
                return USER;
            case "督查人员":
                return SUPERVISION_USER;
            default:
                return WORKER;
        }
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
