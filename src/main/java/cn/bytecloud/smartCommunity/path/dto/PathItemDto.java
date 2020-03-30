package cn.bytecloud.smartCommunity.path.dto;

import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.path.entity.PathType;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.PATH_END_NODE_ID;

@Data
public class PathItemDto {

    private String id;

    private String startNodeId;

    private String endNodeId;

    private String name;

    private PathType type;

    private PathAttribute attribute;

    private long createTime;

    private String desc;

    private String webId;


    public String getCreateTime() {
        return StringUtil.getTime(new Date(createTime));
    }
}
