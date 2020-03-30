package cn.bytecloud.smartCommunity.menu.service;

import cn.bytecloud.smartCommunity.menu.entity.Menu;

import java.util.List;

public interface MenuService {

    Menu findFirstByName(String name);

    void save(Menu tMenu);

    Menu findByPermissionId(String permissionId);

    Object list();

    Menu findFirstByNameAndPid(String name, String pid);

    List<Menu> findByPid(String pid);
}
