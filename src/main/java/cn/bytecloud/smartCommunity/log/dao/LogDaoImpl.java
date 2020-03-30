package cn.bytecloud.smartCommunity.log.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.log.dto.LogPageDto;
import cn.bytecloud.smartCommunity.log.entity.Log;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import java.util.*;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Repository
public class LogDaoImpl extends BaseDao<Log> implements LogDao {
    @Autowired
    private LogRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public void delByWorkId(String workId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.LOG_WORK_ID).is(workId));
        template.remove(query, Log.class);
    }

    @Override
    public long onceCount(long startTime, long endTime,  List<String> unitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.LOG_END_FLAG).is(true));
        query.addCriteria(Criteria.where(ModelConstant.LOG_HANDLER_COUNT).is(3));
        query.addCriteria(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime));
        if (EmptyUtil.isNotEmpty(unitId)) {
            query.addCriteria(Criteria.where(ModelConstant.LOG_UNIT_ID).in(unitId));
        }
        return template.count(query, Log.class);
    }

    @Override
    public long returnCount(long startTime, long endTime, List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.LOG_HANDLER_COUNT).gt(3));
        query.addCriteria(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime));
        if (EmptyUtil.isNotEmpty(unitIds)) {
            query.addCriteria(Criteria.where(ModelConstant.LOG_UNIT_ID).in(unitIds));
        }
        return template.count(query, Log.class);
    }
}
