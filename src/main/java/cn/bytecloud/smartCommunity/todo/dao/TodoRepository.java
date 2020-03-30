package cn.bytecloud.smartCommunity.todo.dao;

import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository  extends MongoRepository<Todo,String>{
    Todo findOneByWorkIdAndType(String id, TodoType accept);
}
