package cn.bytecloud.smartCommunity.log.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
public class LogItem extends BaseEntity {

    @Field(LOG_ITEM_TYPE)
    private LogType logType;

    @Field(LOG_ITEM_TODO_ID)
    private String todoId;

    @Field(LOG_ITEM_PATH_ID)
    private String pathId;

    @Field(LOG_ITEM_TIME)
    private Long time;

    @Field(LOG_ITEM_TIME_TYPE)
    private TimeType timeType;

    @Field(LOG_ITEM_USER_IDS)
    private List<String> userIds;

    @Field(LOG_ITEM_TODO_IDS)
    private List<String> todoIds;
}
