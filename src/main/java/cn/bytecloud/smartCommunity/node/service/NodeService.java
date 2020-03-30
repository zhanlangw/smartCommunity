package cn.bytecloud.smartCommunity.node.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.dto.AddNodeDto;
import cn.bytecloud.smartCommunity.node.dto.NodeItemDto;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeType;

import java.util.List;

public interface NodeService {
    NodeItemDto add(AddNodeDto dto) throws ByteException;

    NodeItemDto item(String id);

    void del(String id);

    List<Node> findByProcessIdAndNodeType(String processId, NodeType start);

    Node findById(String currNodeId);

    List<Node> findByProcessId(String id);
}
