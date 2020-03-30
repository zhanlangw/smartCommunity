package cn.bytecloud.smartCommunity.smallCategory.dto;

import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
@Data
public class UpdSmallCategoryDto  extends AddSmallCategoryDto{
    @NotEmpty
    private String id;

    @Override
    public SmallCategory toData() {
        SmallCategory category = super.toData();
        category.setId(id);
        return category;
    }
}
