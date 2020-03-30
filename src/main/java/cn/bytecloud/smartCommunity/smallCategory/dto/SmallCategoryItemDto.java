package cn.bytecloud.smartCommunity.smallCategory.dto;

import cn.bytecloud.smartCommunity.bigCategory.service.BigCategoryService;
import cn.bytecloud.smartCommunity.log.entity.Log;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

public class SmallCategoryItemDto {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Integer num;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String abbre;

    @Getter
    @Setter
    private TimeType timeType;

    @Setter
    @Getter
    private WorkType workType;

    @Getter
    @Setter
    private Long time;

    @Setter
    private String unitId;

    @Setter
    private String bigCategoryId;


    @Getter
    @Setter
    private String desc;


    public Map getUnit() {
        if (workType == WorkType.ORDINARY) {
            return null;
        }
        Map map = new HashedMap();
        map.put("name", SpringUtils.getBean(UnitService.class).findById(unitId).getName());
        map.put("id", unitId);
        return map;
    }

    public Map getBigCategory() {
        Map map = new HashedMap();
        map.put("name", SpringUtils.getBean(BigCategoryService.class).findById(bigCategoryId).getName());
        map.put("id", bigCategoryId);
        return map;
    }
}
