package cn.bytecloud.smartCommunity.work.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 案卷(工作)
 */
@Document(collection = T_WORK)
@Data
public class Work extends BaseEntity {
    //标题
    @Field(WORK_TITLE)
    private String title;
    @Field(WORK_COUNT)
    private int count;
    //组织机构id
    @Field(WORK_UNIT_ID)
    private String unitId;
    @Field(WORK_RETURN_FLAG)
    private Boolean returnFlag;
    @Field(WORK_DELAY_FLAG)
    private Boolean delayFlag;
    //类型
    @Field(WORK_TYPE_ID)
    private String typeId;
    //是否通过审核
    @Field(WORK_ACCEPT_FLAG)
    private boolean acceptFlag;
    //编号
    @Field(WORK_NUM)
    private String num;
    //流程id
    @Field(WORK_PROCESS_ID)
    private String processId;
    //是否结束
    @Field(WORK_END_FLAG)
    private boolean endFlag;
    //案卷紧急类型
    @Field(WORK_WORK_TYPE)
    private WorkType workType;
    //维度
    @Field(WORK_LATITUDE)
    private String latitude;
    //经度
    @Field(WORK_LONGITUDE)
    private String longitude;
    //发生地质
    @Field(WORK_ADDRESS)
    private String address;
    //来原
    @Field(WORK_SOURCE)
    private WorkSource source;
    //发起退件时当时代办人员,用于退件拒绝时找到对应的办理人
    @Field(WORK_RETURN_USER_IDS)
    private List<String> returnuserIds;
    //处理前照片地质集合
    @Field(WORK_HANDLE_BEFORE_IMAGE_PATHS)
    private List<String> beforeImagePaths = new ArrayList<>();

    //环节id 该字段只是为了记录流程扭转是的环节,并不是当前流程的当前环节,一个案卷会对应多个环节,
////    @Field(WORK_CURR_NODE_ID)
//    @Transient
//    private String currNodeId;

//    //来源路径id
//    @Field(WORK_BEFORE_PATH_ID)
//    private String beforePathId;
    //处理前视屏地质集合
    @Field(WORK_HANDLE_BEFORE_VIDEO_PATHS)
    private List<String> beforeVideoPaths = new ArrayList<>();
    //处理后照片地质集合
    @Field(WORK_HANDLE_AFTER_IMAGE_PATHS)
    private List<String> afterImagePaths = new ArrayList<>();
    //处理后视屏地质集合
    @Field(WORK_HANDLE_AFTER_VIDEO_PATHS)
    private List<String> afterVideoPaths = new ArrayList<>();
    //处理后描述
    @Field(WORK_HANDLE_DESC)
    private String handleDesc;
    //截止时间
    @Field(WORK_END_TIME)
    private long endTime;
    //完成时间
    @Field(WORK_FINISH_TIME)
    private long finishTime;
    //完成人
    @Field(WORK_FINISH_USER_ID)
    private String finishUserid;
    @Field(WORK_BLACKLIST_ID)
    private String blacklistId;
    @Field(WORK_DEVICE_ID)
    private String deviceId;
    @Field(WORK_READER_IDS)
    private Set<String> readerIds;
    @Field(WORK_STATUS)
    private WorkStatus status;
    @Field(WORK_CREATE_YEAR)
    private Integer createYear;
    @Field(WORK_CREATE_MONTH)
    private Integer createMonth;
    @Field(WORK_CREATE_DAY)
    private Integer createDay;
    @Field(WORK_FINISH_YEAR)
    private Integer finishYear;
    @Field(WORK_FINISH_MONTH)
    private Integer finishMonth;
    @Field(WORK_FINISH_DAY)
    private Integer finishDay;

    @Field(WORK_EVENT_ID)
    private String eventId;

    public Work() {
        this.acceptFlag = false;
        this.delayFlag = false;
        this.returnFlag = false;
        this.readerIds = new HashSet<>();
    }

    @Override
    public void setCreateTime(long createTime) {
        super.setCreateTime(createTime);
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(createTime));
        this.createYear = instance.get(Calendar.YEAR);
        this.createMonth = instance.get(Calendar.MONTH) + 1;
        this.createDay = instance.get(Calendar.DAY_OF_MONTH);
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(finishTime));
        this.finishYear = instance.get(Calendar.YEAR);
        this.finishMonth = instance.get(Calendar.MONTH) + 1;
        this.finishDay = instance.get(Calendar.DAY_OF_MONTH);
    }

    public Todo toTodo() {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setWorkId(super.getId());
        todo.setNum(num);
        todo.setWorkSource(source);
        todo.setWorkTypeId(typeId);
        todo.setAddress(address);
        todo.setWorkId(super.getId());
        return todo;
    }
}
