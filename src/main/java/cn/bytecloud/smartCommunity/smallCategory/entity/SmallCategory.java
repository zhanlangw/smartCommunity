package cn.bytecloud.smartCommunity.smallCategory.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = T_SMALL_CATEORY)
public class SmallCategory extends BaseEntity {
    //名字
    @Field(SMALL_CATEORY_NAME)
    private String name;

    @Field(SMALL_CATEORY_ABBRE)
    private String abbre;

    //大类id
    @Field(SMALL_CATEORY_BIG_ID)
    private String bigCategoryId;

    //紧急程度
    @Field(SMALL_CATEORY_TYPE)
    private WorkType workType;

    //职能部门
    @Field(SMALL_CATEORY_UNIT_ID)
    private String unitId;

    //需要处理的时长
    @Field(SMALL_CATEORY_TIME)
    private Long time;

    @Field(SMALL_CATEORY_TIME_TYPE)
    private TimeType timeType;
}
