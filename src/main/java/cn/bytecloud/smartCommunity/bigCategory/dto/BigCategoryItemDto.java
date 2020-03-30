package cn.bytecloud.smartCommunity.bigCategory.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.BIG_CATEGORY_ABBRE;

@Data
public class BigCategoryItemDto {
    private String id;

    private Integer num;

    private String name;
    private String desc;
    private String abbre;

}
