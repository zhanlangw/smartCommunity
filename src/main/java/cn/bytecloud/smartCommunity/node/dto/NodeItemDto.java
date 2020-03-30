package cn.bytecloud.smartCommunity.node.dto;

import cn.bytecloud.smartCommunity.node.entity.HandlerType;
import cn.bytecloud.smartCommunity.node.entity.NodeAttribute;
import cn.bytecloud.smartCommunity.node.entity.NodeButton;
import cn.bytecloud.smartCommunity.node.entity.NodeType;
import cn.bytecloud.smartCommunity.process.service.ProcessService;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NodeItemDto {
    @Getter
    @Setter
    private String id;

    //名称
    @Getter
    @Setter
    private String name;

    //类型
    @Getter
    @Setter
    private NodeType type;

    @Getter
    @Setter
    private String webId;

    //按钮
    @Getter
    @Setter
    private List<NodeButton> buttons;

    //是否能上传文件
    @Getter
    @Setter
    private boolean uploadFlag;

    //流程id
    @Setter
    private String processId;

    @Getter
    @Setter
    private HandlerType handlerType;

    @Setter
    private String unitId;

    @Setter
    private long createTime;

    @Setter
    @Getter
    private String desc;

    @Setter
    @Getter
    private NodeAttribute attribute;


    public Map getProcess() {
        Map map = new HashedMap();
        map.put("name", SpringUtils.getBean(ProcessService.class).findById(processId).getName());
        map.put("id", processId);
        return map;
    }

    public String getCreateTime() {
        return StringUtil.getTime(new Date(createTime));
    }

    public Map getUnit() {
        if (handlerType != HandlerType.CUSTOMIZE) {
            return null;
        }
        Map map = new HashedMap();
        map.put("name", SpringUtils.getBean(UnitService.class).findById(unitId).getName());
        map.put("id", unitId);

        return map;
    }
}
