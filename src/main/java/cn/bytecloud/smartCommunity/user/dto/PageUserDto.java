package cn.bytecloud.smartCommunity.user.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PageUserDto extends BasePageDto {

    @NotEmpty
    private String name;
}
