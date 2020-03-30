package cn.bytecloud.smartCommunity.node.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 环节
 */
@Data
@Document(collection = T_NODE)
public class Node extends BaseEntity {

    //名称
    @Field(NODE_NAME)
    private String name;

    //类型
    @Field(NODE_TYPE)
    private NodeType type;

    @Field(NODE_ATTRIBUTE)
    private NodeAttribute attribute;

    //按钮
    @Field(NODE_BUTTONS)
    private List<NodeButton> buttons;

    //是否能上传文件
    @Field(NODE_UPLOAD_FLAG)
    private boolean uploadFlag;

    //流程id
    @Field(NODE_PROCESS_ID)
    private String processId;

    @Field(NODE_HANDLER_TYPE)
    private HandlerType handlerType;

    @Field(NODE_UNIT_ID)
    private String unitId;

    @Field(NODE_WEB_ID)
    private String webId;

}
