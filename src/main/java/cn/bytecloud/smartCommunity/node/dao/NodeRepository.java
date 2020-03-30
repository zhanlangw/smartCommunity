package cn.bytecloud.smartCommunity.node.dao;

import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeType;
import cn.bytecloud.smartCommunity.path.entity.Path;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NodeRepository extends MongoRepository<Node, String> {
    List<Node> findByProcessIdAndType(String processId, NodeType nodeType);

    List<Node> findByProcessId(String id);
}
