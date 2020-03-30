package cn.bytecloud.smartCommunity.work.dao;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.work.dto.WrokPageDto;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkDao {
    Work save(Work work);

    Optional<Work> findLast();

    long countByTypeId(String typeId);

    Object todoList(WrokPageDto dto, Basis first);

    Object acceptList(WrokPageDto dto);

    Object finishList(WrokPageDto dto);

    List<Work> alarmList(Map<String, String> map);

    Object historyList(WrokPageDto dto);

    List<Work> findByIds(List<String> ids);

    long totalCount(StatsDto dto) throws ByteException;

    long finishCount(StatsDto dto) throws ByteException;

    long unFinishCount(StatsDto dto) throws ByteException;

    long timeOutCount(StatsDto dto) throws ByteException;

    long alarnCount(StatsDto dto,List<String> ids) throws ByteException;

    Object workStats(StatsDto dto, List<Unit> units);

    long count(long startTime, long endTime,  List<String> unitId);

    long countByStatus(long startTime, long endTime,  List<String> unitId, WorkStatus status);

    long finishCount(long startTime, long endTime,  List<String> unitId);

    Object trend(long startTime, long endTime);

    List<HashMap> countByUnitId(long startTime, long endTime, List<String> unitId);

    Map<String, List<HashMap>> trendByTypeId(String typeId, long startTime, long endTime, List<String> unitIds);

    List<Work> map(StatsDto dto);

    void histogram(Unit unit, Long startTime, Long endTime);

    long unFinishCount(Long startTime, Long endTime, List<String> unitIds);

    Long timeOutCount(Long startTime, Long endTime, List<String> unitIds);

    long onTimeCount(long startTime, long endTime, List<String> unitIds);

    Work findAiInspection(String blacklistId, String deviceId, long inspectionTime);
}
