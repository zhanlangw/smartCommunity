package cn.bytecloud.smartCommunity.menu.service;

import cn.bytecloud.smartCommunity.menu.dao.MenuDao;
import cn.bytecloud.smartCommunity.menu.dao.MenuRepository;
import cn.bytecloud.smartCommunity.menu.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao dao;

    @Autowired
    private MenuRepository repository;


    @Override
    public Menu findFirstByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void save(Menu tMenu) {
        repository.save(tMenu);

    }

    @Override
    public Menu findByPermissionId(String permissionId) {
        return repository.findOne(permissionId);
    }

    @Override
    public Object list() {
        return null;
    }

    @Override
    public Menu findFirstByNameAndPid(String name, String pid) {
        return repository.findFirstByNameAndPid(name,pid);
    }

    @Override
    public List<Menu> findByPid(String pid) {
        return repository.findByPid(pid);
    }
}
