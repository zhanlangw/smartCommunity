package cn.bytecloud.smartCommunity.permission.dao;

import cn.bytecloud.smartCommunity.permission.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {


    List<Permission> findByIds(Set<String> permissionIds);

}
