package cn.bytecloud.smartCommunity.stats.dao;

import cn.bytecloud.smartCommunity.stats.entity.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends MongoRepository<Stats,String> {
}
