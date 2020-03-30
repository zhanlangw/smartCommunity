package cn.bytecloud.smartCommunity.stats.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.stats.entity.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatsDaoImpl extends BaseDao<Stats> implements StatsDao {
    @Autowired
    private StatsRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public List<Stats> findByUnitId(long startTime, long endTime, String unitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.STATS_UNIT_ID).is(unitId));
        query.addCriteria(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime));
        return template.find(query, Stats.class);
    }
}
