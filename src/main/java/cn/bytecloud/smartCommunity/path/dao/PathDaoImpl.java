package cn.bytecloud.smartCommunity.path.dao;

import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.process.entity.Process;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PathDaoImpl implements PathDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private PathRepository repository;

    @Override
    public Path save(Path path) {
        if (EmptyUtil.isEmpty(path.getId())) {
            path.setId(UUIDUtil.getUUID());
            path.setCreateTime(System.currentTimeMillis());
            path.setCreatorId(UserUtil.getUserId());
        } else {
            Path old = repository.findOne(path.getId());
            path.setCreateTime(old.getCreateTime());
            path.setCreatorId(old.getCreatorId());
        }
        path.setUpdateTime(System.currentTimeMillis());
        repository.save(path);
        return path;
    }
}
