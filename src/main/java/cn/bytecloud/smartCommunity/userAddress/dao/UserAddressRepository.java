package cn.bytecloud.smartCommunity.userAddress.dao;

import cn.bytecloud.smartCommunity.userAddress.entity.UserAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends MongoRepository<UserAddress,String> {
}
