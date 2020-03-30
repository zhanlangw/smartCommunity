package cn.bytecloud.smartCommunity.todo.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.util.UserUtil;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = T_TODO)
public class Todo extends BaseEntity {
    //工作id
    @Field(TODO_WORK_ID)
    private String workId;

    //代办紧急程度
    @Field(TODO_TYPE)
    private TodoType type;

    @Field(TODO_WORK_TYPE)
    private WorkType workType;

    //案卷标题
    @Field(TODO_TITLE)
    private String title;

    //案卷编号
    @Field(TODO_NUM)
    private String num;

    //案卷来源
    @Field(TODO_WORK_SOURCE)
    private WorkSource workSource;

    //案卷类型id
    @Field(TODO_WORK_TYPE_ID)
    private String workTypeId;

    //案卷发生地点
    @Field(TODO_ADDRESS)
    private String address;

    //当前环节id
    @Field(TODO_CURR_NODE_ID)
    private String currNodeId;

    //来源路径id
    @Field(TODO_BEFORE_PATH_ID)
    private String beforePathId;

    //来源类型
    @Field(TODO_BEFORE_ATTRIBUTE)
    private PathAttribute beforeAttribute;

    @Field(TODO_HANDLER_IDS)
    private List<String> handlerIds;

    //代办结束时间
    @Field(TODO_END_TIME)
    private Long endTime;

    @Field(TODO_TIME_TYPE)
    private TimeType timeType;

    //延时时长
    @Field(TODO_TIME)
    private Long time;

}
