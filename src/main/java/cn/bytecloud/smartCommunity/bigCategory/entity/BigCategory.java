package cn.bytecloud.smartCommunity.bigCategory.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 大类
 */
@Document(collection = T_BIG_CATEGORY)
@Data
public class BigCategory extends BaseEntity {
    @Field(BIG_CATEGORY_NUM)
    private Integer num;

    @Field(BIG_CATEGORY_NAME)
    private String name;

    //简称
    @Field(BIG_CATEGORY_ABBRE)
    private String abbre;
}
