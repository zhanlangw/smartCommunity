package cn.bytecloud.smartCommunity.role.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import lombok.Data;

@Data
public class PageRoleDto extends BasePageDto {

    private String name;

    private String creator;
}

