package cn.bytecloud.smartCommunity.menu.dao;

import cn.bytecloud.smartCommunity.menu.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuRepository extends MongoRepository<Menu,String> {
    Menu findByName(String name);

    Menu findFirstByNameAndPid(String name, String pid);

    List<Menu> findByPid(String pid);
}
