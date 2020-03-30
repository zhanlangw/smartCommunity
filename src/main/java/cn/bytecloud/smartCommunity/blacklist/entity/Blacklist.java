package cn.bytecloud.smartCommunity.blacklist.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = T_BLACKLIST)
public class Blacklist extends BaseEntity {

    @Field(BLACKLIST_NAME)
    private String name;

    @Field(BLACKLIST_TYPE_ID)
    private String typeId;

    @Field(BLACKLIST_IMAGE_PATH)
    private String imagePath;

    @Field(BLACKLIST_LEFT_IMAGE_PATH)
    private String leftImagePath;

    @Field(BLACKLIST_RIGHT_IMAGE_PATH)
    private String rightImagePath;

    @Field
    private String code;
}
