package cn.bytecloud.smartCommunity.smallCategory.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dto.AddSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.SmallCategoryItemDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.UpdSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.work.entity.WorkType;

import java.util.List;

public interface SmallCategoryService {
    SmallCategoryItemDto add(AddSmallCategoryDto dto) throws ByteException;

    SmallCategoryItemDto item(String id);

    SmallCategoryItemDto upd(UpdSmallCategoryDto dto) throws ByteException;

    void del(String id) throws ByteException;

    List<SmallCategory> findByBigId(String bigId);

    Object list(CategoryPageDto dto);

    SmallCategory findById(String id);

    List<SmallCategory> findByName(String categoryName);

    List<SmallCategory> findAll();

    List<SmallCategory> findByWorkType(WorkType urgent);
}
