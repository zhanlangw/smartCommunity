package cn.bytecloud.smartCommunity.log.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.todo.dto.TodoSource;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = T_LOG)
public class Log extends BaseEntity {

    public Log() {
        this.logItems = new LinkedList<>();
        this.endFlag = false;
        super.setId(UUIDUtil.getUUID());
    }

    //工作id
    @Field(LOG_WORK_ID)
    private String workId;

    @Field(LOG_END_FLAG)
    private Boolean endFlag;

    //街区id
    @Field(LOG_UNIT_ID)
    private String unitId;

    //处理次数
    @Field(LOG_HANDLER_COUNT)
    private long handlerCount;

    @Field(LOG_HANDLER_TIME)
    private long handlerTime;

    @Field(LOG_ITEM)
    private LinkedList<LogItem> logItems;
}
