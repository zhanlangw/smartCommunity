package cn.bytecloud.smartCommunity.blacklist.dto;

import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.bigCategory.service.BigCategoryService;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public class BlacklistItemDto {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String imagePath;

    @Getter
    @Setter
    private String leftImagePath;


    @Getter
    @Setter
    private String rightImagePath;

    @Getter
    @Setter
    private String desc;

    @Setter
    private String typeId;

    public Map getType() {
        Map map = new HashedMap();
        map.put("id", typeId);
        SmallCategory smallCategory = SpringUtils.getBean(SmallCategoryService.class).findById(typeId);
        map.put("name", smallCategory.getName());
        BigCategory bigCategory = SpringUtils.getBean(BigCategoryService.class).findById(smallCategory.getBigCategoryId());
        map.put("bigId", bigCategory.getId());
        map.put("bigName", bigCategory.getName());
        return map;
    }
}
