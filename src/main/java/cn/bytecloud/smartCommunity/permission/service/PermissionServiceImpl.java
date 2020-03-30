package cn.bytecloud.smartCommunity.permission.service;

import cn.bytecloud.smartCommunity.menu.entity.Menu;
import cn.bytecloud.smartCommunity.menu.service.MenuService;
import cn.bytecloud.smartCommunity.permission.dao.PermissionDao;
import cn.bytecloud.smartCommunity.permission.dao.PermissionRepository;
import cn.bytecloud.smartCommunity.permission.dto.PermissionTree;
import cn.bytecloud.smartCommunity.permission.entity.Permission;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 版权 @Copyright: 2019 www.bytecloud.cn Inc. All rights reserved.
 * 文件名称： PermissionServiceImpl
 * 包名：cn.bytecloud.smartCommunity.permission.service
 * 创建人：@author wangkn@bytecloud.cn
 * 创建时间：2019/07/04 15:30
 * 修改人：wangkn@bytecloud.cn
 * 修改时间：2019/07/04 15:30
 * 修改备注：
 */

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionDao dao;

    @Autowired
    private PermissionRepository repository;

    @Autowired
    private MenuService menuService;


    @Override
    public void save(Permission perm) {
        repository.save(perm);
    }

    @Override
    public List<Permission> findByIds(Set<String> permissionIds) {
        return dao.findByIds(permissionIds);
    }

    @Override
    public Permission findFirstByInterfaceUrl(String interfaceUrl) {
        return repository.findFirstByInterfaceUrl(interfaceUrl);
    }

    @Override
    public List<PermissionTree> tree() {
        List<Menu> firstMenu = menuService.findByPid(null);
        List<PermissionTree> data = new ArrayList<>();
        firstMenu.forEach(menu -> {
            data.add(toTree(menu));
        });
        return data;
    }

    @Override
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Permission> findByUserId(User user) {
        Set<String> collect = roleService.findByIds(user.getRoleIds()).stream().flatMap(role -> role.getPermissionIds().stream()).collect(Collectors.toSet());
        return dao.findByIds(collect);
    }

    private PermissionTree toTree(Menu menu) {
        PermissionTree tree = toData(menu);
        tree.getTree().addAll(findPermissionByMenuId(menu.getId()));
        for (Menu m : menuService.findByPid(tree.getId())) {
            tree.getTree().add(toTree(m));
        }

        return tree;
    }

    private List<PermissionTree> findPermissionByMenuId(String menuId) {
        List<PermissionTree> list = new ArrayList<>();
        repository.findByMenuId(menuId).forEach(permission -> {
            PermissionTree PermissionTree = toData(permission);
            list.add(PermissionTree);
        });
        return list;
    }

    private PermissionTree toData(Menu menu) {
        PermissionTree data = new PermissionTree();
        data.setId(menu.getId());
        data.setName(menu.getName());
        data.setType(1);
        return data;
    }

    public PermissionTree toData(Permission permission) {
        PermissionTree data = new PermissionTree();
        data.setId(permission.getId());
        data.setName(permission.getName());
        data.setType(2);
        return data;
    }
}
