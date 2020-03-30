package cn.bytecloud.smartCommunity.user.dao;

import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

//    List<User> findByUnitId(String id);

//    List<User> findByUnitIdAndUserType(String unitId, UserType userType);

    List<User> findByUserType(UserType userType);
}
