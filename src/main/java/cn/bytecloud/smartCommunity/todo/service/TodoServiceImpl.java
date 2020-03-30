package cn.bytecloud.smartCommunity.todo.service;

import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.todo.dao.TodoDao;
import cn.bytecloud.smartCommunity.todo.dao.TodoRepository;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.PushMsg;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import cn.bytecloud.smartCommunity.work.dto.SubmitDto;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.bytecloud.smartCommunity.constant.SystemConstant.INIT_SYSTEM_ID;

@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoDao dao;

    @Autowired
    private TodoRepository repository;

    @Autowired
    private WorkService workService;

    @Autowired
    private UserService userService;

    /**
     * 添加代码
     *
     * @param work
     * @param todoType     代办类型
     * @param userIds      代办人id
     * @param dto
     * @param beforePathId
     * @param attribute
     * @param nodeId
     */
    @Override
    public Todo addTodo(Work work, TodoType todoType, List<String> userIds, SubmitDto dto, String beforePathId, PathAttribute attribute, String nodeId) {
        Todo todo = work.toTodo();
        todo.setWorkType(work.getWorkType());
        todo.setTime(dto == null ? null : dto.getTime());
        todo.setTimeType(dto == null ? null : dto.getTimeType());
        todo.setEndTime(work.getEndTime());
        todo.setCurrNodeId(nodeId);
        todo.setBeforePathId(beforePathId);
        todo.setBeforeAttribute(attribute);
        todo.setDesc(dto == null ? null : dto.getDesc());
        todo.setId(UUIDUtil.getUUID());
        todo.setType(todoType);
        todo.setHandlerIds(userIds);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setUpdateTime(System.currentTimeMillis());

        if (work.isAcceptFlag()) {
            todo.setCreatorId(UserUtil.getUserId());
        } else {
            todo.setCreatorId(INIT_SYSTEM_ID);
        }

        repository.save(todo);

        try {
//            if (todoType == TodoType.TODO) {
            PushMsg.pushMsg("您有一条新的待办,请尽快办理!", todo.getHandlerIds(), null);
//            }
        } catch (Exception e) {
            log.info("app消息推送失败!!    todoId::" + todo.getId());
        }
        return todo;
    }

    @Override
    public Optional<Todo> findById(String id) {
        Todo todo = repository.findOne(id);
        return Optional.ofNullable(todo);
    }

    @Override
    public void delByWorkId(String workId) {
        dao.delByWorkId(workId);
    }

    @Override
    public void delById(String todoId) {
        repository.delete(todoId);
    }

    @Override
    public List<Todo> findByWorkIdNeTodoType(String wordId, TodoType todoType) {
        return dao.findByWorkIdNeTodoType(wordId, todoType);
    }

    @Override
    public List<Todo> findByWorkId(String workId) {
        return dao.findByWorkId(workId);
    }

    @Override
    public void save(List<Todo> data) {
        repository.save(data);
    }

    @Override
    public void delByWorkIdAndNeType(String workId, TodoType todoType) {
        dao.delByWorkIdAndNeType(workId, todoType);
    }

    @Override
    public void updateEndTime(Work work) {
        dao.updateEndTime(work);
    }

    @Override
    public void delByIds(String ids) {
        dao.delByIds(ids);
    }

    @Override
    public Optional<Todo> findByWorkIdAndTodoType(String id, TodoType accept) {
        return Optional.ofNullable(repository.findOneByWorkIdAndType(id, accept));
    }

    @Override
    public Object homeList() {
        List<Todo> todos = dao.homeList();
        List<Map> data = new ArrayList<>();
        todos.forEach(todo -> {
            Map map = new HashedMap<>();
            map.put("id", todo.getId());
            map.put("title", todo.getTitle());
            map.put("type", todo.getType());
            map.put("workType", todo.getWorkType());
            Work work = workService.findById(todo.getWorkId());
            map.put("creator", work.getSource() == WorkSource.SYSTEM ? WorkSource.SYSTEM.getEnumValue() : userService.findById(work.getCreatorId()).getName());
            map.put("createTime", StringUtil.getTime(new Date(work.getCreateTime())));
            data.add(map);
        });
        return data;
    }

    @Override
    public void delByWorkIdAndType(String id, TodoType type) {
        dao.delByWorkIdAndType(id, type);
    }

    @Override
    public void save(Todo todo) {
        repository.save(todo);
    }
}
