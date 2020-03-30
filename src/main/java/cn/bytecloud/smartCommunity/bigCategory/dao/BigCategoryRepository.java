package cn.bytecloud.smartCommunity.bigCategory.dao;

import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BigCategoryRepository extends MongoRepository<BigCategory,String> {
    List<BigCategory> findByName(String name);
    BigCategory findOneByName(String name);

    List<BigCategory> findByAbbre(String abbre);
}
