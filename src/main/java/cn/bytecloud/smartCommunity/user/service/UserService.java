package cn.bytecloud.smartCommunity.user.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.user.dto.*;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
    UserItemInUnitDto add(AddUserInUnitDto dto) throws ByteException;

    UserItemInUnitDto itemInUnit(String id);

    void del(String id) throws ByteException;

    void resetPasswrod(ResetPasswordDto dto) throws ByteException;

    UserItemInUnitDto updInUnit(UpdUserInUnitDto dto) throws ByteException;

    UserItemDto item(String id);

    UserItemDto upd(UpdUserDto dto) throws ByteException;

    User findByUsername(String username);

    User save(User user);


    Map login(LoginDto dto);

    User findById(String creatorId);

    List<User> findByUnitId(String id);

    List<User> findByRoleId(String id);

    List<User> findWorkerByUnitId(String unitId);

    List<User> findByIds(List<String> handlerIds);

    void updUserPermissionCache(String id);

    Set<String> findPermission(User userId);

    List<User> findByUnitIdS(List<String> unitIds);

    List<User> findAllByUnitIdAndUserType(String unitId,UserType userType);

    List<User> findByUserType(UserType userType);

    List<User> findAllByUnitId(String unitId);

    void updImage(String id, String image);

    Object list(PageUserDto name);

    void importUsers(String path) throws ByteException, IOException;

    void updpassword(UpdPasswordDto dto) throws ByteException;

    List<User> findByUserTypeAndUnitIds(UserType worker, List<String> ids);

    void openAlarmSound();

    void closeAlarmSound();

    boolean sound();

    void logout();

}
