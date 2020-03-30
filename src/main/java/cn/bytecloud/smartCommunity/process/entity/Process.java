package cn.bytecloud.smartCommunity.process.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
@Data
@Document(collection = T_PROCESS)
public class Process extends BaseEntity {
    //名字
    @Field(PROCESS_NAME)
    private String name;

    //源
    @Field(PROCESS_SOURCE)
    private String source;

    //版本
    @Field(PROCESS_VERSION)
    private Integer version;

    //web样式
    @Field(PROCESS_STYLE)
    private String style;

    public Map toDto() {
        Map map = new HashedMap();
        map.put("id", super.getId());
        map.put("name", name);
        map.put("desc", super.getDesc());
        map.put("style", style);
        return map;
    }
}
