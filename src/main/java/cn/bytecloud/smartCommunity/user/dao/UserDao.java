package cn.bytecloud.smartCommunity.user.dao;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.user.dto.*;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;

import java.util.List;

public interface UserDao {
    User save(User user);

    void resetPasswrod(String id, String md5);

    UserItemInUnitDto updInUnit(UpdUserInUnitDto dto);

    UserItemDto upd(UpdUserDto dto);

    List<User> findByRoleId(String roleId);

    List<User> findWorkerByUnitId(String unitId, UserType userType);

    List<User> findByIds(List<String> handlerIds);

    List<User> findByUnitIds(List<String> unitIds);

    void updImage(String id, String image);

    PageModel<User> list(PageUserDto dto);

    List<User> findByUnitId(String id);

    List<User> findByUserTypeAndUnitIds(UserType worker, List<String> unitIds);

    List<User> findByUnitIdAndUserType(String unitId, UserType userType);

    void updateSoundFlag(boolean flag);

}
