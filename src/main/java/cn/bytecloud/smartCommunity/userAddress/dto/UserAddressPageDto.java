package cn.bytecloud.smartCommunity.userAddress.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;

@Data
public class UserAddressPageDto {
    @NotEmpty
    private String userId;

    private String startTime;

    private String endTime;

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
