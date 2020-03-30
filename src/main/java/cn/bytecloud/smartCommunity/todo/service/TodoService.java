package cn.bytecloud.smartCommunity.todo.service;

import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.work.dto.SubmitDto;
import cn.bytecloud.smartCommunity.work.entity.Work;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    Todo addTodo(Work work, TodoType review,  List<String>  userIds, SubmitDto dto, String beforePathId, PathAttribute attribute, String nodeId);

    Optional<Todo> findById(String id);

    void delByWorkId(String workId);

    void delById(String todoId);

    List<Todo> findByWorkIdNeTodoType(String id, TodoType delay);

    List<Todo>  findByWorkId(String workId);

    void save(List<Todo> data);

    void delByWorkIdAndNeType(String id, TodoType delay);

    void updateEndTime(Work work);

    void delByIds(String ids);

    Optional<Todo> findByWorkIdAndTodoType(String id, TodoType accept);

    Object homeList();

    void delByWorkIdAndType(String id, TodoType type);

    void save(Todo todo);
}
