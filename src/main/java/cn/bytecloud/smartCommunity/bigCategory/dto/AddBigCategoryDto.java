package cn.bytecloud.smartCommunity.bigCategory.dto;

import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddBigCategoryDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    private Integer num;

    //简称
    @NotEmpty
    @Length(max = 2, min = 2)
    private String abbre;

    private String desc;

    public BigCategory toData() {
        BigCategory bigCategory = new BigCategory();
        bigCategory.setName(name);
        bigCategory.setNum(num == null ? 99 : num);
        bigCategory.setDesc(desc);
        bigCategory.setAbbre(abbre);
        return bigCategory;
    }
}
