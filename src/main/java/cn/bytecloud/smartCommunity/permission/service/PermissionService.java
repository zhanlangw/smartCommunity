package cn.bytecloud.smartCommunity.permission.service;

import cn.bytecloud.smartCommunity.permission.entity.Permission;
import cn.bytecloud.smartCommunity.user.entity.User;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    void save(Permission perm);

    List<Permission> findByIds(Set<String> permissionIds);

    Permission findFirstByInterfaceUrl(String interfaceUrl);

    Object tree();

    List<Permission> findAll();

    List<Permission> findByUserId(User id);
}
