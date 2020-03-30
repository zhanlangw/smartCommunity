package cn.bytecloud.smartCommunity.todo.dao;

import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.work.entity.Work;

import java.util.List;

public interface TodoDao {

    void delByWorkId(String workId);

    List<Todo> findByWorkIdNeTodoType(String wordId, TodoType todoType);

    List<Todo> findByWorkId(String workId);

    void delByWorkIdAndNeType(String workId, TodoType todoType);

    void updateEndTime(Work work);

    void delByIds(String ids);

    List<Todo> homeList();

    void delByWorkIdAndType(String id, TodoType type);
}
