package cn.bytecloud.smartCommunity.blacklist.dao;

import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistRepository extends MongoRepository<Blacklist,String> {
    List<Blacklist> findByTypeId(String typeId);
}
