package cn.bytecloud.smartCommunity.path.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.path.dao.PathDao;
import cn.bytecloud.smartCommunity.path.dao.PathRepository;
import cn.bytecloud.smartCommunity.path.dto.AddPathDto;
import cn.bytecloud.smartCommunity.path.dto.PathItemDto;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathServiceImpl implements PathService {
    @Autowired
    private PathRepository repository;
    @Autowired
    private PathDao dao;

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @Override
    public PathItemDto add(AddPathDto dto) throws ByteException {
        return EntityUtil.entityToDto(dao.save(dto.toData()), PathItemDto.class);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public PathItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), PathItemDto.class);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public void del(String ids) {
        for (String id : ids.split(",")) {
            repository.delete(id);
        }

    }

    /**
     * 查询流程对应的路径
     *
     * @param id
     * @return
     */
    @Override
    public List<Path> findByProcessId(String id) {
        return repository.findByProcessId(id);
    }

    @Override
    public Path findById(String afterPathId) {
        return repository.findOne(afterPathId);
    }

    /**
     * 获取环节下的所有路径
     *
     * @param processId
     * @param nodeId
     * @return
     */
    @Override
    public List<Path> findPathByNodeId(String nodeId, String processId) {
        return repository.findByProcessIdAndStartNodeId(processId, nodeId);
    }
}
