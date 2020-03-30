package cn.bytecloud.smartCommunity.record.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.work.entity.Work;

import java.util.Optional;

public interface RecordService {
    void save(Work work, Todo todo, Path afterPath, Node node, long createTime) throws ByteException;

    void saveEnd(Work work, Todo todo, long createTime);

    Optional<Record> findByWorkIdAndTodoId(String workId, String todoId);

    void updateEndTime(Work work);

    void delete(String id);

    void delByWorkId(String id);
}
