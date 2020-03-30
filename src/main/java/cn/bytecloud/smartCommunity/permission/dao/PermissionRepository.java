package cn.bytecloud.smartCommunity.permission.dao;

import cn.bytecloud.smartCommunity.permission.entity.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Permission findFirstByInterfaceUrl(String interfaceUrl);

    List<Permission> findByMenuId(String id);
}