package cn.bytecloud.smartCommunity.role.service;

import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.permission.service.PermissionService;
import cn.bytecloud.smartCommunity.role.dao.RoleDao;
import cn.bytecloud.smartCommunity.role.dao.RoleRepository;
import cn.bytecloud.smartCommunity.role.dto.AddRoleDto;
import cn.bytecloud.smartCommunity.role.dto.PageRoleDto;
import cn.bytecloud.smartCommunity.role.dto.RoleItemDto;
import cn.bytecloud.smartCommunity.role.dto.UpdRoleDto;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleDao dao;

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private UnitService unitService;

    @Override
    public Role findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public Object save(AddRoleDto dto) throws ByteException {
        if (repository.findByName(dto.getName()) != null) {
            throw new ByteException("名称重复");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), RoleItemDto.class);
    }

    @Override
    public Object item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), RoleItemDto.class);
    }

    @Override
    public Object upd(UpdRoleDto dto) throws ByteException {
        Role role = repository.findByName(dto.getName());
        if (role != null && !role.getId().equals(dto.getId())) {
            throw new ByteException("名称重复");
        }

        RoleItemDto roleItemDto = EntityUtil.entityToDto(dao.save(dto.toData()), RoleItemDto.class);
        ThreadPool.threadPool.execute(() -> {
            updUserPermissionCache(dto.getId());
        });
        return roleItemDto;
    }

    /**
     * 修改对应用户的权限缓存
     *
     * @param id
     */
    private void updUserPermissionCache(String id) {
        userService.updUserPermissionCache(id);
    }

    @Override
    public void del(String ids) throws ByteException {
        for (String id : ids.split(",")) {
            if (userService.findByRoleId(id).size() > 0) {
                throw new ByteException(repository.findOne(id).getName() + "已被引用,禁止删除");
            }
            repository.delete(id);
        }
    }

    @Override
    public Object list(PageRoleDto dto) {

        return dao.list(dto);
    }

    @Override
    public Object permissionTree() {
        return permissionService.tree();
    }

    @Override
    public Object deivceTree() {
        return unitService.deivceTree();
    }

    @Override
    public Role findFirstByName(String workRoleName) {
        return repository.findFirstByName(workRoleName);
    }

    @Override
    public List<Role> findByIds(Set<String> roleIds) {
        return dao.findByIds(roleIds);
    }
}
