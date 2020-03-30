package cn.bytecloud.smartCommunity.bigCategory.dto;

import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdBigCategoryDto extends AddBigCategoryDto {
    @NotEmpty
    private String id;

    @Override
    public BigCategory toData() {
        BigCategory bigCategory = super.toData();
        bigCategory.setId(id);
        return bigCategory;
    }
}
