package cn.bytecloud.smartCommunity.user.dto;


import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UserItemInUnitDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private UserType userType;

    /**
     * 用户姓名
     */
    @Setter
    @Getter
    private String name;

    @Getter
    @Setter
    private Integer num;

    /**
     * 登录帐号
     */
    @Setter
    @Getter
    private String username;

    /**
     * 角色
     */
    @Setter
    private Set<String> roleIds;
    /**
     * 地址
     */
    @Setter
    @Getter
    private String address;
    /**
     * 联系电话
     */
    @Setter
    @Getter
    private String telephone;
    /**
     * 上一级组织机构
     */
    @Setter
    private List<String> unitIds;
    /**
     * 备注信息
     */
    @Setter
    @Getter
    private String desc;

    //重写了Role的get方法,如果在这个对象中引用到Role对象的话,就是一个个的map形式.
    public List<Map> getRole() {
        if (SystemConstant.INIT_ROOT_USERNAME.equals(username) || SystemConstant.INIT_ADMIN_USERNAME.equals(username)) {
            return null;
        }
        List<Map> list = new ArrayList<>();
        SpringUtils.getBean(RoleService.class).findByIds(roleIds).forEach(role -> {
            Map map = new HashedMap();
            map.put("name", role.getName());
            map.put("id", role.getId());
            list.add(map);
        });
        return list;
    }

    //重写了Unit的get方法,再查询unit的时候,返回的就是一个map
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


}
