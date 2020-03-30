package cn.bytecloud.smartCommunity.smallCategory.dao;

import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallCategoryRepository extends MongoRepository<SmallCategory,String> {
    List<SmallCategory> findByName(String name);
    SmallCategory findOneByName(String name);

    List<SmallCategory> findByBigCategoryId(String bigId);

    List<SmallCategory> findByWorkType(WorkType workType);

    List<SmallCategory> findByAbbre(String abbre);
}
