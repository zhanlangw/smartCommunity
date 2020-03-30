package cn.bytecloud.smartCommunity.role.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.permission.entity.Permission;
import cn.bytecloud.smartCommunity.role.dto.AddRoleDto;
import cn.bytecloud.smartCommunity.role.dto.PageRoleDto;
import cn.bytecloud.smartCommunity.role.dto.UpdRoleDto;
import cn.bytecloud.smartCommunity.role.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {


    Role findOne(String id);

    Object save(AddRoleDto dto) throws ByteException;

    Object item(String id);

    Object upd(UpdRoleDto dto) throws ByteException;

    void del(String id) throws ByteException;

    Object list(PageRoleDto dto);

    Object permissionTree();

    Object deivceTree();

    Role findFirstByName(String workRoleName);

    List<Role> findByIds(Set<String> roleIds);

}
