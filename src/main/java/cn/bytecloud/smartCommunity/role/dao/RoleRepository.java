package cn.bytecloud.smartCommunity.role.dao;

import cn.bytecloud.smartCommunity.role.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,String>{
    Role findByName(String name);

    Role findFirstByName(String workRoleName);
}
