package cn.bytecloud.smartCommunity.stats.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.stats.dto.AppHistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.HistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.RankingDto;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.stats.entity.Stats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StatsService {
    Object dataBroadcast(StatsDto dto) throws ByteException;

    Object onlineRate();

    Object workStats(StatsDto dto);

    Object ability(String unitId);

    Object trend(String unitId, int count);

    Object histogram(HistogramStatsDto dto);

    Object map(StatsDto dto);

    List<Stats> findByUnitId(long startTime, long endTime, String unitId);

    void exportHistogram(HistogramStatsDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException;

    List<Map<String, String>> score(String time, String unitId);

    void exportScore(String time, HttpServletRequest request, HttpServletResponse response) throws IOException;

    Object appHistogram(AppHistogramStatsDto dto);

    Object ranking(List<RankingDto> dtos);
}
