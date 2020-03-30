package cn.bytecloud.smartCommunity.smallCategory.service;

import cn.bytecloud.smartCommunity.blacklist.service.BlacklistService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dao.SmallCategoryDao;
import cn.bytecloud.smartCommunity.smallCategory.dao.SmallCategoryRepository;
import cn.bytecloud.smartCommunity.smallCategory.dto.AddSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.SmallCategoryItemDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.UpdSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmallCategoryServiceImpl implements SmallCategoryService {
    @Autowired
    private WorkService workService;

    @Autowired
    private BlacklistService blacklistService;


    @Autowired
    private SmallCategoryDao dao;
    @Autowired
    private SmallCategoryRepository repository;

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @Override
    public SmallCategoryItemDto add(AddSmallCategoryDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).size() > 0) {
            throw new ByteException("名字已存在");
        }
        if (repository.findByAbbre(dto.getAbbre()).size() > 0) {
            throw new ByteException("简称已存在");
        }
        if (dto.getWorkType() == WorkType.URGENT && EmptyUtil.isEmpty(dto.getUnitId())) {
            throw new ByteException("紧急案卷类型必须选择职能部门");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), SmallCategoryItemDto.class);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SmallCategoryItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), SmallCategoryItemDto.class);
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @Override
    public SmallCategoryItemDto upd(UpdSmallCategoryDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).stream().anyMatch(category -> !category.getId().equals(dto.getId()))) {
            throw new ByteException("名字已存在");
        }
        if (repository.findByAbbre(dto.getAbbre()).stream().anyMatch(category -> !category.getId().equals(dto.getId()))) {
            throw new ByteException("简称已存在");
        }
        if (dto.getWorkType() == WorkType.URGENT && EmptyUtil.isEmpty(dto.getUnitId())) {
            throw new ByteException("紧急案卷类型必须选择职能部门");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), SmallCategoryItemDto.class);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public void del(String ids) throws ByteException {
        for (String id : ids.split(",")) {
            if (workService.countByTypeId(id) > 0 || blacklistService.findByTypeId(id).size() > 0) {
                throw new ByteException(repository.findOne(id).getName() + "已被引用,禁止删除");
            }
            repository.delete(id);
        }
    }

    @Override
    public List<SmallCategory> findByBigId(String bigId) {
        return repository.findByBigCategoryId(bigId);
    }

    @Override
    public Object list(CategoryPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public SmallCategory findById(String id) {
        return repository.findOne(id);
    }

    @Override
    public List<SmallCategory> findByName(String categoryName) {
        return dao.findByName(categoryName);
    }

    @Override
    public List<SmallCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<SmallCategory> findByWorkType(WorkType workType) {
        return repository.findByWorkType(workType);
    }
}
