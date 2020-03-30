package cn.bytecloud.smartCommunity.bigCategory.service;

import cn.bytecloud.smartCommunity.bigCategory.dto.AddBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.BigCategoryItemDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.BigCategoryPageDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.UpdBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.exception.ByteException;

import java.util.List;

public interface BigCategoryService {
    BigCategoryItemDto add(AddBigCategoryDto dto) throws ByteException;

    BigCategoryItemDto item(String id);

    BigCategoryItemDto upd(UpdBigCategoryDto dto) throws ByteException;

    List<BigCategoryPageDto> list();

    BigCategory findById(String bigSmallCategoryId);

    void del(String id) throws ByteException;
}
