package cn.bytecloud.smartCommunity.version.dao;

import cn.bytecloud.smartCommunity.version.entity.Version;
import cn.bytecloud.smartCommunity.version.entity.VersionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends MongoRepository<Version,String> {
    Version findOneByType(VersionType type);
}
