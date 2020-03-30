package cn.bytecloud.smartCommunity.bigCategory.dao;

import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BigCategoryDaoImpl implements BigCategoryDao {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private BigCategoryRepository repository;

    /**
     * 保存
     * @param bigCategory
     * @return
     */
    @Override
    public BigCategory save(BigCategory bigCategory) {
        if (EmptyUtil.isEmpty(bigCategory.getId())) {
            bigCategory.setId(UUIDUtil.getUUID());
            bigCategory.setCreatorId(UserUtil.getUserId());
            bigCategory.setCreateTime(System.currentTimeMillis());
        } else {
            BigCategory old = repository.findOne(bigCategory.getId());
            bigCategory.setCreateTime(old.getCreateTime());
            bigCategory.setCreatorId(old.getCreatorId());
        }
        bigCategory.setUpdateTime(System.currentTimeMillis());
        repository.save(bigCategory);
        return bigCategory;
    }
}
