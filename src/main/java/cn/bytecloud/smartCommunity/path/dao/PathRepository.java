package cn.bytecloud.smartCommunity.path.dao;

import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.path.dto.PathItemDto;
import cn.bytecloud.smartCommunity.path.entity.Path;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathRepository extends MongoRepository<Path, String> {

    List<Path> findByProcessId(String id);

    List<Path> findByProcessIdAndStartNodeId(String processId, String nodeId);
}
