package cn.bytecloud.smartCommunity.stats.dao;

import cn.bytecloud.smartCommunity.stats.entity.Stats;

import java.util.List;

public interface StatsDao {
    List<Stats> findByUnitId(long startTime, long endTime, String unitId);
}
