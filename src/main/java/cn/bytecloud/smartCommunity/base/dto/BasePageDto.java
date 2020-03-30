package cn.bytecloud.smartCommunity.base.dto;

import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Data
public class BasePageDto {
    @NotNull
    @Min(0)
    private Integer start;

    @NotNull
    @Min(0)
    private Integer count;

    private String startTime;

    private String endTime;

    private String creator;

    public Long getStartTime() {
        if (EmptyUtil.isEmpty(startTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(startTime)).getTime();
    }

    public Long getEndTime() {
        if (EmptyUtil.isEmpty(endTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(endTime)).getTime();
    }
}
