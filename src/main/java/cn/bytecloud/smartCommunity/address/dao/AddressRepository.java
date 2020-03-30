package cn.bytecloud.smartCommunity.address.dao;

import cn.bytecloud.smartCommunity.address.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {
}
