package cn.bytecloud.smartCommunity.record.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 记录表,用于撤回
 */
@Data
@Document(collection = T_RECORD)
public class Record  extends BaseEntity{
    //记录类型
    @Field(RECORD_TYPE)
    private RecordType type;

    //代办id
    @Field(RECORD_TODO_ID)
    private String todoId;

    //申请延时时间,
    @Field(RECORD_TIME)
    private Long time;

    //代办数据
    @Field(RECORD_DATA)
    private List<Todo> data = new ArrayList<>();

    //案卷id
    @Field(RECORD_WORK_ID)
    private String workId;


}
