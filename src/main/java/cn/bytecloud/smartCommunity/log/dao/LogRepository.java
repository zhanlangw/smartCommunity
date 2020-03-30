package cn.bytecloud.smartCommunity.log.dao;

import cn.bytecloud.smartCommunity.log.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<Log,String> {
    Log findOneByWorkId(String workId);
}
