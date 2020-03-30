package cn.bytecloud.smartCommunity.log.dao;

import cn.bytecloud.smartCommunity.log.dto.LogPageDto;

import java.util.List;

public interface LogDao {
    void delByWorkId(String workId);

    long onceCount(long startTime, long endTime,  List<String> unitId);

    long returnCount(long startTime, long endTime, List<String> unitIds);

}
