package cn.bytecloud.smartCommunity.log.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.log.entity.Log;
import cn.bytecloud.smartCommunity.log.entity.LogType;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;

import java.util.List;

public interface LogService {
    Log save(LogType logType, String pathId, String workId, String todoId, String desc, Long time, TimeType timeType, List<String> userIds, List<String> todoIds, long
            createTime);

    Log save(LogType logType, String pathId, String workId, String todoId, String desc, Long time, TimeType timeType, List<String> userIds, List<String> todoIds, long
            createTime,Boolean endFlag);

    void delByWorkId(String id);

    Object list(String id, Integer type) throws ByteException;

    long onceCount(long startTime, long endTime,  List<String> unitId);

    long returnCount(long startTime, long endTime, List<String> unitIds);

    Log findOneByWorkId(String workId);
}
