package cn.bytecloud.smartCommunity.role.dao;

import cn.bytecloud.smartCommunity.role.dto.PageRoleDto;
import cn.bytecloud.smartCommunity.role.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {

    List<Role> findByIds(Set<String> roleIds);

    Role save(Role role);

    Object list(PageRoleDto dto);
}
