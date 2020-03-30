package cn.bytecloud.smartCommunity.record.dao;

import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.work.entity.Work;

import java.util.List;

public interface RecordDao {
    List<Record> updateEndTime(Work work);

    void delByWorkId(String id);

    void delByTodoId(String id);
}
