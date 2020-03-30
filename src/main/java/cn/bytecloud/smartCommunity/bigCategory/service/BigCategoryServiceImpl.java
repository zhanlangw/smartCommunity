package cn.bytecloud.smartCommunity.bigCategory.service;

import cn.bytecloud.smartCommunity.bigCategory.dao.BigCategoryDao;
import cn.bytecloud.smartCommunity.bigCategory.dao.BigCategoryRepository;
import cn.bytecloud.smartCommunity.bigCategory.dto.AddBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.BigCategoryItemDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.BigCategoryPageDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.UpdBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigCategoryServiceImpl implements BigCategoryService {
    @Autowired
    private BigCategoryDao dao;

    @Autowired
    private BigCategoryRepository repository;

    @Autowired
    private SmallCategoryService smallCategoryService;

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @Override
    public BigCategoryItemDto add(AddBigCategoryDto dto) throws ByteException {
        BigCategory bigCategory = dto.toData();
        if (repository.findByName(dto.getName()).size() > 0) {
            throw new ByteException("名字已存在");
        }
        if (repository.findByAbbre(dto.getAbbre()).size() > 0) {
            throw new ByteException("简称已存在");
        }
        return EntityUtil.entityToDto(dao.save(bigCategory), BigCategoryItemDto.class);

    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public BigCategoryItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), BigCategoryItemDto.class);
    }

    /**
     * 修改
     * @param dto
     * @return
     */
    @Override
    public BigCategoryItemDto upd(UpdBigCategoryDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).stream().anyMatch(bigCategory -> !bigCategory.getId().equals(dto.getId()))) {
            throw new ByteException("名字已存在");
        }
        if (repository.findByAbbre(dto.getAbbre()).stream().anyMatch(bigCategory -> !bigCategory.getId().equals(dto.getId()))) {
            throw new ByteException("简称已存在 ");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), BigCategoryItemDto.class);
    }

    /**
     * 列表
     *
     * @return
     */
    @Override
    public List<BigCategoryPageDto> list() {
        return EntityUtil.entityListToDtoList(repository.findAll(new Sort(Sort.Direction.ASC, ModelConstant.BIG_CATEGORY_NUM)), BigCategoryPageDto.class);
    }

    /**
     * 根据id查询
     *
     * @param bigSmallCategoryId
     * @return
     */
    @Override
    public BigCategory findById(String bigSmallCategoryId) {
        return repository.findOne(bigSmallCategoryId);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public void del(String ids) throws ByteException {
        for (String id : ids.split(",")) {
            if (smallCategoryService.findByBigId(id).size() > 0) {
                throw new ByteException(repository.findOne(id).getName() + "已被引用,禁止删除");
            }
            repository.delete(id);
        }

    }
}
