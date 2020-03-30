package cn.bytecloud.smartCommunity.node.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.node.dao.NodeDao;
import cn.bytecloud.smartCommunity.node.dao.NodeRepository;
import cn.bytecloud.smartCommunity.node.dto.AddNodeDto;
import cn.bytecloud.smartCommunity.node.dto.NodeItemDto;
import cn.bytecloud.smartCommunity.node.entity.HandlerType;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeType;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    private NodeRepository repository;
    @Autowired
    private NodeDao dao;

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @Override
    public NodeItemDto add(AddNodeDto dto) throws ByteException {
        Node node = dao.save(dto.toData());
        return EntityUtil.entityToDto(node, NodeItemDto.class);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public NodeItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), NodeItemDto.class);
    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void del(String ids) {
        for (String id : ids.split(",")) {
            repository.delete(id);
        }
    }

    @Override
    public List<Node> findByProcessIdAndNodeType(String processId, NodeType nodeType) {
        return repository.findByProcessIdAndType(processId,nodeType);
    }

    @Override
    public Node findById(String id) {
        return repository.findOne(id);
    }

    @Override
    public List<Node> findByProcessId(String id) {
        return repository.findByProcessId(id);
    }
}
