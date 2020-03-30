package cn.bytecloud.smartCommunity.log.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class LogPageDto extends BasePageDto {
    @NotEmpty
    private String workId;
}
