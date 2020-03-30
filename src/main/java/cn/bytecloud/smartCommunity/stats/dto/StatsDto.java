package cn.bytecloud.smartCommunity.stats.dto;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class StatsDto {

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;

    private String unitId;

    public void setStartTime(String startTime) throws ByteException {
        if (EmptyUtil.isEmpty(startTime)) {
            this.startTime = null;
        }
        this.startTime = Objects.requireNonNull(StringUtil.getTime(startTime.trim() + " 00:00:00")).getTime();
    }

    public void setEndTime(String endTime) throws ByteException {
        if (EmptyUtil.isEmpty(endTime)) {
            this.endTime = null;
        }
        this.endTime = Objects.requireNonNull(StringUtil.getTime(endTime.trim() + " 23:59:59")).getTime();
    }
}
