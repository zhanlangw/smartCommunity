package cn.bytecloud.smartCommunity.work.dto;

import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.bigCategory.service.BigCategoryService;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.blacklist.service.BlacklistService;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeButton;
import cn.bytecloud.smartCommunity.node.service.NodeService;
import cn.bytecloud.smartCommunity.record.service.RecordService;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WorkItemDto implements Serializable {

    @Setter
    private int type;

    @Getter
    @Setter
    private String workId;

    @Getter
    @Setter
    private String id;

    //标题
    @Getter
    @Setter
    private String title;

    //编号
    @Getter
    @Setter
    private String num;

    //类型ID
    @Setter
    private transient String typeId;

    @Setter
    private String unitId;


    //流程id
    @Getter
    @Setter
    private String processId;

    @Getter
    @Setter
    private boolean acceptFlag;


    //案卷紧急类型
    @Setter
    @Getter
    private WorkType workType;

    //发生地质
    @Getter
    @Setter
    private String address;

    //来源
    @Getter
    @Setter
    private WorkSource source;

    //处理前照片地质集合
    @Getter
    @Setter
    private List<String> beforeImagePaths;

    //处理前视屏地质集合
    @Getter
    @Setter
    private List<String> beforeVideoPaths;

    //处理后照片地质集合
    @Getter
    @Setter
    private List<String> afterImagePaths;

    //处理后视屏地质集合
    @Getter
    @Setter
    private List<String> afterVideoPaths;

    //处理后视屏地质集合
    @Getter
    @Setter
    private String handleDesc;

    @Setter
    private String creatorId;

    @Setter
    private long createTime;

    @Setter
    private long finishTime;

    @Setter
    private long endTime;

    //当前环节id
    @Setter
    private String currNodeId;

    @Getter
    @Setter
    private String desc;

    @Setter
    private String todoId;

    @Setter
    private Long currentTime;

    @Setter
    private String blacklistId;

    @Getter
    @Setter
    private TodoType todoType;

    public String getBlacklistName() {
        if (EmptyUtil.isNotEmpty(blacklistId)) {
            Blacklist blacklist = SpringUtils.getBean(BlacklistService.class).findById(blacklistId);
            return blacklist == null ? "" : blacklist.getName();
        }
        return "";
    }

    public String getType() {
        SmallCategory smallCategory = SpringUtils.getBean(SmallCategoryService.class).findById(typeId);
        BigCategory bigCategory = SpringUtils.getBean(BigCategoryService.class).findById(smallCategory.getBigCategoryId());
        return bigCategory.getName() + "-" + smallCategory.getName();
    }

    public String getCreator() {
        if (WorkSource.SYSTEM == source) {
            return WorkSource.SYSTEM.getEnumValue();
        }
        return SpringUtils.getBean(UserService.class).findById(creatorId).getName();
    }

    public String getCreateTime() {
        return StringUtil.getTime(createTime == 0 ? null : new Date(createTime));
    }

    public String getFinishTime() {
        return StringUtil.getTime(finishTime == 0 ? null : new Date(finishTime));
    }

    public String getEndTime() {
        return StringUtil.getTime(endTime == 0 ? null : new Date(endTime));
    }

    public List<NodeButton> getButton() {
        if (EmptyUtil.isEmpty(currNodeId)) {
            return new ArrayList<>();
        }
        List<NodeButton> buttons = new ArrayList<>();
        if (!acceptFlag) {
            buttons = new ArrayList<>();
            buttons.add(NodeButton.ACCEPT);
            buttons.add(NodeButton.DELETE);
        } else {
            Node node = SpringUtils.getBean(NodeService.class).findById(currNodeId);
            if (type == 1) {
                buttons = node.getButtons();
            }
        }
        return buttons;
    }

    public String getStatus() {
        long time;
        if (currentTime == null) {
            time = System.currentTimeMillis();
        } else {
            time = currentTime;
        }
        if (endTime > 0) {
            return WorkStatus.getStatus(endTime, SpringUtils.getBean(BasisService.class).findFirst(), time).name();
        }
        return "";
    }

    public Map getUnit(){
        Map<String,String> map = new HashedMap<>();
        map.put("id", unitId);
        map.put("name", SpringUtils.getBean(UnitService.class).findById(unitId).getName());
        return map;
    }
}
