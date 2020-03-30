package cn.bytecloud.smartCommunity.work.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.stats.dto.AppHistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.HistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.work.dto.*;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;

import java.util.List;
import java.util.Map;

public interface WorkService {
    List<Work> findByProcessId(String id);

    void add(AddWorkDto dto) throws ByteException;

    void del(String ids);

    WorkItemDto item(String id, Integer type) throws ByteException;

    Object getPaths(String id) throws ByteException;

    void accept(boolean flag, String id) throws ByteException;

    long countByTypeId(String id);

    void submit(SubmitDto dto) throws ByteException;

    void end(String id) throws ByteException;

    void withdraw(String workId, String todoId, String pathId) throws ByteException;

    void add(AddWorkFromDeviceDto dto,boolean modelFlag) throws ByteException;

    Object todoList(WrokPageDto dto);

    void todoDel(String ids);

    void finishDel(String ids);

    Object acceptList(WrokPageDto dto);

    Object finishList(WrokPageDto dto);

    Object historyList(WrokPageDto dto);

    Object alarmList();

    Work findById(String workId);

    Object homeList();

    Object gpsUnit(String latitude, String longitude) throws ByteException;

    void updImage(UpdImageDto dto) throws ByteException;

    Object dataBroadcast(StatsDto dto) throws ByteException;

    Object workStats(StatsDto dto);

    long count(long startTime, long endTime,  List<String> unitId);

    long countByStatus(long startTime, long endTime,  List<String> unitId, WorkStatus status);

    long finishCount(long startTime, long endTime,  List<String> unitId);

    Object trend(String unitId, int count);

    List<Work> map(StatsDto dto);

    List<Map<String, Map<String, Object>>> histogram(HistogramStatsDto dto);

    long unFinishCount(Long startTime, Long endTime, List<String> unitIds);

    long timeOutCount(long startTime, long endTime, List<String> unitIds);

    long onTimeCount(long startTime, long endTime, List<String> unitIds);

    Object appHistogram(AppHistogramStatsDto dto);

}
