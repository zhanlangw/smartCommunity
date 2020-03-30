package cn.bytecloud.smartCommunity.record.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeType;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.record.dao.RecordDao;
import cn.bytecloud.smartCommunity.record.dao.RecordRepository;
import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.record.entity.RecordType;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.todo.service.TodoService;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao dao;
    @Autowired
    private RecordRepository repository;
    @Autowired
    private TodoService todoService;

    /**
     * 生成记录
     *  @param work
     * @param todo
     * @param node
     * @param createTime
     */
    @Override
    public void save(Work work, Todo todo, Path afterPath, Node node, long createTime) throws ByteException {
        Record record = new Record();
        record.setCreateTime(createTime);
        record.setWorkId(work.getId());
        record.setTodoId(todo.getId());

        if (todo.getType() == TodoType.DELAY ) {
            if (afterPath.getAttribute() != PathAttribute.REFUSE && afterPath.getAttribute() != PathAttribute.AGREE) {
                throw new ByteException("处理申请延时路径只能是同意或者拒绝");
            }
            if (afterPath.getAttribute() == PathAttribute.AGREE) {
                int num =todo.getTimeType() == TimeType.DAY ? 24 : 1;
                record.setTime( todo.getTime() * 3600 * 1000 * num);
            }
            record.setType(RecordType.DELAY);
            record.getData().add(todo);
        }else if (afterPath.getAttribute() == PathAttribute.DELAY){
            record.setType(RecordType.DELAY);
            record.getData().add(todo);
        }else if (todo.getType() == TodoType.RETURN && afterPath.getAttribute() == PathAttribute.AGREE) {
            record.setType(RecordType.ORDINARY);
            dao.delByTodoId(todo.getId());
            record.getData().addAll(todoService.findByWorkId(work.getId()));
        } else {
            if (node.getType() == NodeType.END) {
                dao.delByWorkId(work.getId());
                record.setType(RecordType.END);
                record.getData().addAll(todoService.findByWorkId(work.getId()));
            } else {
                record.setType(RecordType.ORDINARY);
                record.getData().add(todo);
                dao.delByTodoId(todo.getId());
            }
        }

        Record old = repository.findOneByWorkIdAndType(work.getId(), record.getType());
        if (old == null) {
            record.setId(UUIDUtil.getUUID());
        } else {
            record.setId(old.getId());
        }
        repository.save(record);
    }

    @Override
    public void saveEnd(Work work, Todo todo, long createTime) {
        Record record = new Record();
        record.setCreateTime(createTime);
        record.setWorkId(work.getId());
        record.setType(RecordType.END);
        record.getData().addAll(todoService.findByWorkId(work.getId()));
        record.setTodoId(todo.getId());

        Record old = repository.findOneByWorkIdAndType(work.getId(), record.getType());
        if (old == null) {
            record.setId(UUIDUtil.getUUID());
        } else {
            record.setId(old.getId());
        }
        repository.save(record);
    }

    @Override
    public Optional<Record> findByWorkIdAndTodoId(String workId, String todoId) {
        return repository.findOneByWorkIdAndTodoId(workId, todoId);
    }

    @Override
    public void updateEndTime(Work work) {
        List<Record> list = dao.updateEndTime(work);
        list.forEach(record -> {
            record.getData().forEach(todo -> {
                todo.setEndTime(work.getEndTime());
            });
        });
        repository.save(list);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public void delByWorkId(String id) {
        dao.delByWorkId(id);
    }
}
