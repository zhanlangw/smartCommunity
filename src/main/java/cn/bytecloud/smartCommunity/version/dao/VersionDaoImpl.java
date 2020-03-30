package cn.bytecloud.smartCommunity.version.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.version.entity.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VersionDaoImpl extends BaseDao<Version> implements Versiondao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private VersionRepository repository;
}
