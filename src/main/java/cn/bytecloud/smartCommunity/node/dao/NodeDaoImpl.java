package cn.bytecloud.smartCommunity.node.dao;

import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.path.dao.PathRepository;
import cn.bytecloud.smartCommunity.process.entity.Process;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class NodeDaoImpl implements NodeDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private NodeRepository repository;

    @Override
    public Node save(Node node) {
        if (EmptyUtil.isEmpty(node.getId())) {
            node.setId(UUIDUtil.getUUID());
            node.setCreateTime(System.currentTimeMillis());
            node.setCreatorId(UserUtil.getUserId());
        } else {
            Node old = repository.findOne(node.getId());
            node.setCreateTime(old.getCreateTime());
            node.setCreatorId(old.getCreatorId());
        }
        node.setUpdateTime(System.currentTimeMillis());
        repository.save(node);
        return node;
    }
}
