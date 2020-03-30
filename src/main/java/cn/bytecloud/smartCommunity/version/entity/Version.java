package cn.bytecloud.smartCommunity.version.entity;

import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = ModelConstant.T_VERSION)
@Data
public class Version {

    @Id
    private String id;

    @Field(ModelConstant.VERSION_TYPE)
    private VersionType type;

    @Field(ModelConstant.VERSION_URL)
    private String url;

    @Field(ModelConstant.VERSION_NUM)
    private String num;
}
