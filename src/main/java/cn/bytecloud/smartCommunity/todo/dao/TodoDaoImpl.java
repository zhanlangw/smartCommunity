package cn.bytecloud.smartCommunity.todo.dao;

import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.util.UserUtil;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TodoDaoImpl implements TodoDao {
    @Autowired
    private TodoRepository repository;
    @Autowired
    private MongoTemplate template;
    /**
     * 删除
     * @param workId
     */
    @Override
    public void delByWorkId(String workId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(workId));
        template.remove(query, Todo.class);
    }

    @Override
    public List<Todo> findByWorkIdNeTodoType(String workId, TodoType todoType) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(workId));
        query.addCriteria(Criteria.where(ModelConstant.TODO_TYPE).ne(todoType));
        return template.find(query, Todo.class);
    }

    @Override
    public List<Todo> findByWorkId(String workId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(workId));
        return template.find(query, Todo.class);
    }

    @Override
    public void delByWorkIdAndNeType(String workId, TodoType todoType) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(workId));
        query.addCriteria(Criteria.where(ModelConstant.TODO_TYPE).ne(todoType));
        template.remove(query, Todo.class);
    }

    @Override
    public void updateEndTime(Work work) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(work.getId()));
        Update update = new Update();
        update.set(ModelConstant.TODO_END_TIME, work.getEndTime());
        template.updateMulti(query, update, Todo.class);
    }

    @Override
    public void delByIds(String ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).in(Arrays.stream(ids.split(",")).collect(Collectors.toList())));
        template.remove(query, Todo.class);
    }

    @Override
    public List<Todo> homeList() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        query.addCriteria(Criteria.where(ModelConstant.TODO_HANDLER_IDS).elemMatch(new Criteria().in(UserUtil.getUserId())));
        query.limit(5);
        return template.find(query, Todo.class);
    }

    @Override
    public void delByWorkIdAndType(String id, TodoType type) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.TODO_WORK_ID).is(id));
        query.addCriteria(Criteria.where(ModelConstant.TODO_TYPE).is(type));
        template.remove(query, Todo.class);
    }
}
