package cn.bytecloud.smartCommunity.stats.controller;

import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.stats.dto.AppHistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.HistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.RankingDto;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {


    @Autowired
    private StatsService service;


    /**
     * 数据播报
     *
     * @return
     */
    @GetMapping("/data/broadcast")
    public APIResult dataBroadcast(@Validated StatsDto dto) throws ByteException {
        return APIResult.success().setValue(service.dataBroadcast(dto));
    }

    /**
     * 在线率
     *
     * @return
     */
    @GetMapping("/online/rate")
    public APIResult onlineRate() throws ByteException {
        return APIResult.success().setValue(service.onlineRate());
    }

    /**
     * 街区工作统计
     *
     * @return
     */
    @GetMapping("/work")
    public APIResult workStats(@Validated StatsDto dto) throws ByteException {
        return APIResult.success().setValue(service.workStats(dto));
    }

    /**
     * 综合执法能力
     *
     * @return
     */
    @GetMapping("/ability")
    public APIResult ability(String unitId) {
        return APIResult.success().setValue(service.ability(unitId));
    }

    /**
     * 趋势图
     *
     * @return
     */
    @GetMapping("/trend")
    public APIResult trend(String unitId, Integer count) {
        return APIResult.success().setValue(service.trend(unitId, count == null ? 1 : count));
    }

    /**
     * 热力图
     *
     * @return
     */
    @GetMapping("/map")
    public APIResult map(@Validated StatsDto dto) {
        return APIResult.success().setValue(service.map(dto));
    }

    /**
     * 案卷统计
     *
     * @return
     */
    @GetMapping("/histogram")
    public APIResult histogram(@Validated HistogramStatsDto dto) {
        return APIResult.success().setValue(service.histogram(dto));
    }

    /**
     * 案卷统计
     *
     * @return
     */
    @GetMapping("/app/histogram")
    public APIResult appHistogram(@Validated AppHistogramStatsDto dto) {
        return APIResult.success().setValue(service.appHistogram(dto));
    }

    /**
     * 导出案卷统计
     *
     * @return
     */
    @GetMapping("/histogram/export")
    public void exportHistogram(@Validated HistogramStatsDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        service.exportHistogram(dto, response, request);
    }


    /**
     * 评分
     *
     * @return
     */
    @GetMapping("/score")
    public APIResult score(@RequestParam String time, @RequestParam String unitId) throws IOException {
        return APIResult.success().setValue(service.score(time, unitId));
    }

    /**
     * 排名
     *
     * @return
     */
    @PostMapping("/score/ranking")
    public APIResult ranking(@Validated @RequestBody List<RankingDto> dtos) throws IOException {
        return APIResult.success().setValue(service.ranking(dtos));
    }


    /**
     * 评分导出
     *
     * @return
     */
    @GetMapping("/score/export")
    public void exportScore(@RequestParam String time, HttpServletResponse response, HttpServletRequest request) throws IOException {
        service.exportScore(time, request, response);
    }
}
