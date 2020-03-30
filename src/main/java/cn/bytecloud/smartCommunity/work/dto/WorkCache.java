package cn.bytecloud.smartCommunity.work.dto;

import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.service.NodeService;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.path.service.PathService;
import cn.bytecloud.smartCommunity.process.entity.Process;
import cn.bytecloud.smartCommunity.process.service.ProcessService;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class WorkCache {
    private String id;

    //标题
    private String title;

    //组织机构
    private Unit unit;

    //类型
    private SmallCategory category;

    //编号
    private String num;

    //流程id
    private Process process;

    //案卷紧急类型
    private WorkType workType;

    //维度
    private String latitude;

    //维度
    private String longitude;

    //发生地质
    private String address;

    //发生地质
    private WorkSource source;

    //当前处理人
//    private List<User> handlers;

    //当前环节
    private Node currNode;

    //来源路径id
    private Path beforePath;

    //处理前照片地质集合
    private List<String> beforeImagePaths;

    //处理前视屏地质集合
    private List<String> beforeVideoPaths;

    //处理后照片地质集合
    private List<String> afterImagePaths;

    //处理后视屏地质集合
    private List<String> afterVideoPaths;

    //处理后视屏地质集合
    private String handleDesc;

    //截止时间
    private long endTime;

    private boolean endFlag;

    //案卷完成时间
    private long finishTime;

    private String desc;

    private long createTime;

    private String creatorId;

    public void init(Work work) {
        this.id = work.getId();
        this.title = work.getTitle();
        this.unit = SpringUtils.getBean(UnitService.class).findById(work.getUnitId());
        this.category = SpringUtils.getBean(SmallCategoryService.class).findById(work.getTypeId());
        this.num = work.getNum();
        this.process = SpringUtils.getBean(ProcessService.class).findById(work.getProcessId());
        this.workType = work.getWorkType();
        this.latitude = work.getLatitude();
        this.longitude = work.getLongitude();
        this.address = work.getAddress();
        this.source = work.getSource();
//        this.handlers = SpringUtils.getBean(UserService.class).findByIds(work.getHandlerIds());
//        this.currNode = SpringUtils.getBean(NodeService.class).findById(work.getCurrNodeId());
//        this.beforePath = EmptyUtil.isEmpty(work.getBeforePathId()) ? null : SpringUtils.getBean(PathService.class).findById(work.getBeforePathId());
        this.beforeImagePaths = work.getBeforeImagePaths();
        this.beforeVideoPaths = work.getBeforeVideoPaths();
        this.afterImagePaths = work.getAfterImagePaths();
        this.afterVideoPaths = work.getAfterVideoPaths();
        this.handleDesc = work.getHandleDesc();
        this.desc = work.getDesc();
        this.endTime = work.getEndTime();
        this.finishTime = work.getFinishTime();
        this.createTime = work.getCreateTime();
        this.creatorId = work.getCreatorId();
        this.endFlag = work.isEndFlag();
    }

    public Work toData() {
        Work work = new Work();
        work.setAcceptFlag(true);
        work.setId(id);
        work.setTitle(title);
        work.setUnitId(unit.getId());
        work.setTypeId(category.getId());
        work.setNum(num);
        work.setProcessId(process.getId());
        work.setWorkType(workType);
        work.setLatitude(latitude);
        work.setLongitude(longitude);
        work.setAddress(address);
        work.setSource(source);
//        work.setHandlerIds(handlers.stream().map(User::getId).collect(Collectors.toList()));
//        work.setCurrNodeId(currNode.getId());
//        work.setBeforePathId(beforePath.getId());
        work.setBeforeVideoPaths(beforeVideoPaths);
        work.setBeforeImagePaths(beforeImagePaths);
        work.setAfterImagePaths(afterImagePaths);
        work.setAfterVideoPaths(afterVideoPaths);
        work.setHandleDesc(handleDesc);
        work.setDesc(desc);
        work.setEndTime(endTime);
        work.setFinishTime(finishTime);
        work.setCreatorId(creatorId);
        work.setCreateTime(createTime);
        work.setEndFlag(endFlag);
        return work;
    }
}
