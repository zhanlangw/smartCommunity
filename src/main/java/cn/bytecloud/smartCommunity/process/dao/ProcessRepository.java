package cn.bytecloud.smartCommunity.process.dao;

import cn.bytecloud.smartCommunity.process.entity.Process;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends MongoRepository<Process,String> {
        List<Process> findByName(String name);

}
