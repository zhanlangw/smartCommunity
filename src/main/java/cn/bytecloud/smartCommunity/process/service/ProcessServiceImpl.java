package cn.bytecloud.smartCommunity.process.service;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.node.service.NodeService;
import cn.bytecloud.smartCommunity.path.service.PathService;
import cn.bytecloud.smartCommunity.process.dao.ProcessDao;
import cn.bytecloud.smartCommunity.process.dao.ProcessRepository;
import cn.bytecloud.smartCommunity.process.dto.*;
import cn.bytecloud.smartCommunity.process.entity.Process;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessDao dao;

    @Autowired
    private ProcessRepository repository;

    @Autowired
    private WorkService workService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private PathService PathService;

    /**
     * 添加流程
     *
     * @param dto
     * @throws ByteException
     */
    @Override
    public Map add(AddProcessDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).size() > 0) {
            throw new ByteException("名称重复", ErrorCode.FAILURE);
        }
        Process process = dto.toData();
        process.setSource(UUIDUtil.getUUID());
        process.setVersion(1);
        process = dao.save(dto.toData());
        return process.toDto();
    }

    /**
     * 详情
     *
     * @param id 流程id
     * @return 流程详情
     */
    @Override
    public Map item(String id) {
        return repository.findOne(id).toDto();
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @Override
    public Map upd(UpdProcessDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).stream().anyMatch(process -> !process.getId().equals(dto.getId()))) {
            throw new ByteException("名称重复", ErrorCode.FAILURE);
        }
        Process process = dto.toData();

        Process old = repository.findOne(process.getId());
//        //版本管理
//        if (workService.findByProcessId(process.getId()).size() > 0) {
//            process.setVersion(old.getVersion() == null ? 0 : old.getVersion() + 1);
//            process.setSource(old.getSource());
//            process.setId(null);
//        }

        process.setStyle(old.getStyle());
        process = dao.save(process);
        return process.toDto();
    }

    /**
     * 删除
     *
     * @param ids 多个用逗号分开
     */
    @Override
    public void del(String ids) throws ByteException {
        for (String id : ids.split(",")) {
            if (workService.findByProcessId(id).size() > 0) {
                throw new ByteException(repository.findOne(id).getName() + "已被引用,禁止删除");
            }
            repository.delete(id);
        }
    }

    /**
     * 修改web流程图样式
     *
     * @param dto
     */
    @Override
    public void updStyle(UpdProcessStyleDto dto) {
        dao.updateStyle(dto);
    }

    /**
     * 列表
     *
     * @param dto
     * @return
     */
    @Override
    public Object list(ProcessPageDto dto) {
        PageModel pageModel = dao.list(dto);
        pageModel.setValue(EntityUtil.entityListToDtoList(pageModel.getValue(), ProcessPageListDto.class));
        return pageModel;
    }

    /**
     * 查询最新的流程
     *
     * @return
     */
    @Override
    public Process findFirst() {
        return dao.findFirst();
    }

    @Override
    public Process findById(String processId) {
        return repository.findOne(processId);
    }


    /**
     * webid 和环节id映射,
     *
     * @param id
     * @return
     */
    @Override
    public Object ids(String id) {
        Map map = new HashedMap();
        nodeService.findByProcessId(id).forEach(node -> map.put(node.getWebId(), node.getId()));
        PathService.findByProcessId(id).forEach(path -> map.put(path.getWebId(), path.getId()));
        return map;
    }
}
