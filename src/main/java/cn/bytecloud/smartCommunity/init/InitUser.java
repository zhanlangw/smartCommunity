package cn.bytecloud.smartCommunity.init;


import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.MD5Util;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.SystemConstant.*;

@Slf4j
@Component
public class InitUser {

    @Autowired
    private UserService userService;

    @Autowired
    private UnitService unitService;

    @PostConstruct
    public void init() {
        //初始化组织机构
        Unit unit = null;
        List<Unit> list = unitService.findByPid(null);
        if (list.size() == 0) {
            unit = new Unit();
            unit.setName(INIT_UNIT_NAME);
            unit.setId(UUIDUtil.getUUID());
            unit.setPid(null);
            unit.setCreateTime(System.currentTimeMillis());
            unit.setUpdateTime(System.currentTimeMillis());
            unitService.save(unit);
        } else {
            unit = list.get(0);
        }

        //初始化用户
        if (userService.findByUsername(INIT_ROOT_USERNAME) == null) {
            User user = new User();
            user.setUserflag(true);
            user.setUserType(UserType.ROOT);
            user.setUsername(INIT_ROOT_USERNAME);
            user.setName(INIT_ROOT_USERNAME);
            user.setPassword(MD5Util.getMD5(INIT_ROOT_PASSWORD));
            user.setId(UUIDUtil.getUUID());
            ArrayList<String> unitIds = new ArrayList<>();
            unitIds.add(unit.getId());
            user.setUnitIds(unitIds);
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            userService.save(user);

        }
        if (userService.findByUsername(INIT_ADMIN_USERNAME) == null) {
            User user = new User();
            user.setUserflag(true);
            user.setUserType(UserType.ADMIN);
            user.setUsername(INIT_ADMIN_USERNAME);
            user.setName(INIT_ADMIN_USERNAME);
            user.setPassword(MD5Util.getMD5(INIT_ADMIN_PASSWORD));
            user.setId(UUIDUtil.getUUID());
            ArrayList<String> unitIds = new ArrayList<>();
            unitIds.add(unit.getId());
            user.setUnitIds(unitIds);
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            userService.save(user);
        }
        if (userService.findByUsername(INIT_SYSTEM_USERNAME) == null) {
            User user = new User();
            user.setUserflag(true);
            user.setUsername(INIT_SYSTEM_USERNAME);
            user.setName(INIT_SYSTEM_NAME);
            user.setPassword(MD5Util.getMD5(INIT_SYSTEM_PASSWORD));
            user.setId(INIT_SYSTEM_ID);
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            userService.save(user);
        }
    }
}
