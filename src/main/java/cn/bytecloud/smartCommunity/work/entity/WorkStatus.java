package cn.bytecloud.smartCommunity.work.entity;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.util.BaseEnum;

/**
 * 案卷状态
 */
public enum WorkStatus implements BaseEnum {
    //
    ORDINARY(1, "正常/提前完成"), TIME_OUT(2, "超时"), COMING_SOON_TIME_OUT(3, "即将超时/按时完成");

    private Integer type;
    private String value;

    WorkStatus(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static WorkStatus getStatus(long entTime, Basis basis, long currentTime) {
        if (currentTime > entTime) {
            return WorkStatus.TIME_OUT;
        } else if (currentTime < (entTime - basis.getTimeOut() * 3600 * 1000)) {
            return WorkStatus.ORDINARY;
        } else {
            return WorkStatus.COMING_SOON_TIME_OUT;
        }
    }

    @Override
    public Integer getEnumType() {
        return type;
    }

    @Override
    public String getEnumValue() {
        return value;
    }
}
