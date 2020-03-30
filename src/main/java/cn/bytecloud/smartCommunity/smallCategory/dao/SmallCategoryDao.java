package cn.bytecloud.smartCommunity.smallCategory.dao;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;

import java.util.HashMap;
import java.util.List;

public interface SmallCategoryDao {
    SmallCategory save(SmallCategory smallCategory);

    PageModel<HashMap> list(CategoryPageDto dto);

    List<SmallCategory> findByName(String categoryName);
}
