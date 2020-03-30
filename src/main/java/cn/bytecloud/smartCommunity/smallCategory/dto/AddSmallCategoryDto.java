package cn.bytecloud.smartCommunity.smallCategory.dto;

import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class AddSmallCategoryDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotEmpty
    @Length(max = 2,min = 2)
    private String abbre;

    @NotEmpty
    private String bigCategoryId;

    @NotNull
    private WorkType workType;

    @NotNull
    private Long time;

    private String unitId;

    @NotNull
    private TimeType timeType;

    @Length(max = 100)
    private String desc;

    public SmallCategory toData() {
        return EntityUtil.entityToDto(this, SmallCategory.class);
    }
}
