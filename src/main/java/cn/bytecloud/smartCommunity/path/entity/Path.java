package cn.bytecloud.smartCommunity.path.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
@Data
@Document(collection = T_PATH)
public class Path extends BaseEntity {
    //名字
    @Field(PATH_NAME)
    private String name;

    @Field(PATH_WEB_ID)
    private String webId;

    //名字
    @Field(PATH_TYPE)
    private PathType type;

    //开始环节id
    @Field(PATH_START_NODE_ID)
    private String startNodeId;

    //结束环节id
    @Field(PATH_END_NODE_ID)
    private String endNodeId;

    //属性
    @Field(PATH_ATTRIBUTE)
    private PathAttribute attribute;

    //流程id
    @Field(PATH_PROCESS_ID)
    private String processId;
}
