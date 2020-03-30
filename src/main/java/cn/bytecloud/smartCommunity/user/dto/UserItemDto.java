package cn.bytecloud.smartCommunity.user.dto;

import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 个人中心主页展示
 */

public class UserItemDto {

    @Setter
    @Getter
    private String id;

    @Getter
    @Setter
    private UserType userType;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String imagePath;

    @Setter
    @Getter
    private String gender;

    @Setter
    @Getter
    private String birthday;

    @Setter
    @Getter
    private String age;

    @Setter
    @Getter
    private String telephone;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String address;

    @Setter
    private List<String> unitIds;

    public List<Map> getUnit() {
        UnitService unitService = SpringUtils.getBean(UnitService.class);

        List<Map> list = new ArrayList<>();
        for (String unitId : unitIds) {
            Map map = new HashedMap();
            map.put("name", unitService.findById(unitId).getName());
            map.put("id", unitId);
            list.add(map);
        }
        return list;

    }

    @Setter
    private Set<String> roleIds;

    public List<Map> getRole() {
        List<Map> list = new ArrayList<>();
        for (String roleId : roleIds) {
            Map map = new HashedMap();
            map.put("name", SpringUtils.getBean(RoleService.class).findOne(roleId).getName());
            map.put("id", roleId);
            list.add(map);
        }
        return list;
    }


}
