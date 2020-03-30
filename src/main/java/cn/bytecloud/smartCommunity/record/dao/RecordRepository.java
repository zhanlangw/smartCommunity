package cn.bytecloud.smartCommunity.record.dao;

import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.record.entity.RecordType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
    List<Record> findByWorkId(String workId);

    Record findOneByWorkIdAndType(String workId, RecordType type);

    Optional<Record> findOneByWorkIdAndTodoId(String workId, String todoId);
}
