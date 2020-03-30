package cn.bytecloud.smartCommunity.work.dao;

import cn.bytecloud.smartCommunity.work.entity.Work;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends MongoRepository<Work, String> {
    List<Work> findByProcessId(String id);

    Work findFirstByBlacklistIdAndDeviceIdAndAcceptFlag(String blacklistId, String deviceId, boolean acceptFlag);

    Work findFirstByBlacklistIdAndDeviceIdAndAcceptFlagAndEndFlag(String blacklistId, String deviceId, boolean acceptFlag, boolean endFlag);
}
