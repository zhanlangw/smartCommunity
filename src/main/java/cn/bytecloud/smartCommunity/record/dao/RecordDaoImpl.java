package cn.bytecloud.smartCommunity.record.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.work.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecordDaoImpl extends BaseDao<Record> implements RecordDao {
    @Autowired
    private RecordRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public List<Record> updateEndTime(Work work) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.RECORD_WORK_ID).is(work.getId()));
        return template.find(query, Record.class);
    }

    @Override
    public void delByWorkId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.RECORD_WORK_ID).is(id));
        template.remove(query, Record.class);
    }

    @Override
    public void delByTodoId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.RECORD_TODO_ID).is(id));
        template.remove(query, Record.class);
    }
}
