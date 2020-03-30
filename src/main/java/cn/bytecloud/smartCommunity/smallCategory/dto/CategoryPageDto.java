package cn.bytecloud.smartCommunity.smallCategory.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CategoryPageDto extends BasePageDto {
    private String id;
    private String name;
}
