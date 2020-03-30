package cn.bytecloud.smartCommunity.stats.service;

import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.log.service.LogService;
import cn.bytecloud.smartCommunity.stats.dao.StatsDao;
import cn.bytecloud.smartCommunity.stats.dao.StatsRepository;
import cn.bytecloud.smartCommunity.stats.dto.AppHistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.HistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.RankingDto;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.stats.entity.Stats;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.userAddress.service.UserAddressService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.ExcelUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository repository;
    @Autowired
    private StatsDao dao;

    @Autowired
    private UnitService unitService;
    @Autowired
    private WorkService workService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LogService logService;


    public List<Map<String, String>> getStatsTime() {
        List<Map<String, String>> list = new ArrayList<>();
        Date start = StringUtil.getStatsTime(new SimpleDateFormat("yyyy/MM").format(new Date()) + "/01 00:00:00");
        Calendar calendar = Calendar.getInstance();
        assert start != null;
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, 1);
        Date end = calendar.getTime();

        int num = 0;
        do {
            Map<String, String> map = new HashMap<>();
            map.put("start", start.getTime() + "");
            map.put("end", end.getTime() + "");
            map.put("time", new SimpleDateFormat("yyyy/MM").format(start));
            list.add(map);

            end = start;
            calendar.setTime(start);
            calendar.add(Calendar.MONTH, -1);
            start = calendar.getTime();
            num++;
        } while (num < 6);
        return list;

    }

    /**
     * 数据播报
     *
     * @param dto
     * @return
     */
    @Override
    public Object dataBroadcast(StatsDto dto) throws ByteException {
        return workService.dataBroadcast(dto);
    }

    @Override
    public Object onlineRate() {
        long time = StringUtil.getStatsTime(new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + " 00:00:00").getTime();
        Map<String, java.io.Serializable> map = new HashedMap<String, java.io.Serializable>();
        int userCount = userService.findByUserType(UserType.WORKER).size();
        map.put("userCount", userCount);
        long fateCount = addressService.onlineCount(time);
        map.put("onlineCount", fateCount);
        map.put("userOnlineRate", userCount == 0 ? 0 : String.format("%.0f", fateCount * 100.0 / userCount) + "%");
        long deviceCount = deviceService.count();
        long onlineCount = deviceService.countByOnline();
        map.put("deviceCount", onlineCount + "/" + deviceCount);
        map.put("deviceOnlineRate", deviceCount == 0 ? 0 : String.format("%.0f", onlineCount * 100.0 / deviceCount) + "%");
        return map;
    }

    @Override
    public Object workStats(StatsDto dto) {
        return workService.workStats(dto);
    }

    /**
     * time yyyy/mm/dd hh:mm:ss
     *
     * @param unitId
     * @return
     */
    @Override
    public Object ability(String unitId) {
        List<CompletableFuture<Map<String, Object>>> data = getStatsTime().stream().map(item -> CompletableFuture.supplyAsync(() -> {
            String time = item.get("time");
            long startTime = Long.parseLong(item.get("start"));
            long endTime = Long.parseLong(item.get("end"));

            List<String> unitIds = unitService.findAllByPid(unitId);
            unitIds.add(unitId);

            //总数
            long totalCount = workService.count(startTime, endTime, unitIds);

            //按时处置
            long onTimeCount = workService.countByStatus(startTime, endTime, unitIds, WorkStatus.COMING_SOON_TIME_OUT);

            //提前完成
            long advanceCount = workService.countByStatus(startTime, endTime, unitIds, WorkStatus.ORDINARY);

            //一次完成
            long onceCount = logService.onceCount(startTime, endTime, unitIds);

            //处置数量
            long finishCount = workService.finishCount(startTime, endTime, unitIds);

            //在线率
            double average = dao.findByUnitId(startTime, endTime, unitId).stream().mapToDouble(Stats::getUserOnline).summaryStatistics().getAverage();

            Map<String, Object> map = new HashedMap<>();
            map.put("totalCount", totalCount);
            map.put("onTimeCount", onTimeCount);
            map.put("advanceCount", advanceCount);
            map.put("onceCount", onceCount);
            map.put("finishCount", finishCount);
                map.put("onlineRate", String.format("%.2f", average ) + "%");
            map.put("time", time);
            return map;
        }, ThreadPool.threadPool)).collect(Collectors.toList());
        List<Object> reslut = new ArrayList<>();
        for (CompletableFuture<Map<String, Object>> item : data) {
            try {
                reslut.add(item.get(5000, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                log.info("统计失败！");
            }
        }
        return reslut;
    }

    @Override
    public Object trend(String unitId, int count) {
        return workService.trend(unitId, count);
    }

    @Override
    public Object histogram(HistogramStatsDto dto) {
        return workService.histogram(dto);
    }

    @Override
    public Object appHistogram(AppHistogramStatsDto dto) {
        return workService.appHistogram(dto);
    }

    @Override
    public Object ranking(List<RankingDto> dtos) {
        //排序
        List<RankingDto> list = dtos.stream().sorted(Comparator.comparingDouble(RankingDto::getScore)).collect(Collectors.toList());
        Collections.reverse(list);

        List<Map> data = new ArrayList<>();
        int num = 0;
        String score = null;
        for (int i = 0; i < list.size(); i++) {
            RankingDto item = list.get(i);
            String itemScore = item.getScore() + "";
            if (!itemScore.equals(score)) {
                num++;
            }
            score = itemScore;

            Map<String, Object> map = new HashMap<>();
            map.put(item.getUnitId(), num);
            data.add(map);
        }
        return data;
    }

    @Override
    public Object map(StatsDto dto) {
        return workService.map(dto).stream().map(work -> {
            Map<String, String> map = new HashMap<>();
            map.put("longitude", work.getLongitude());
            map.put("latitude", work.getLatitude());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Stats> findByUnitId(long startTime, long endTime, String unitId) {
        return dao.findByUnitId(startTime, endTime, unitId);
    }

    @Override
    public void exportHistogram(HistogramStatsDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<Map<String, Map<String, Object>>> data = workService.histogram(dto);
        Vector<Vector<String>> rowName = new Vector<>();
        for (Map<String, Map<String, Object>> item : data) {
            Vector<String> row = new Vector<>();
            Map<String, Object> stats = item.get("stats");
            Map<String, Object> histogram = item.get("histogram");

            Map<String, Object> unit = (Map<String, Object>) histogram.get("unit");
            row.add(unit.get("name") + "");

            long totalCount = (long) stats.get("totalCount");
            long finishCount = (long) stats.get("finishCount");

            row.add(totalCount + "");
            row.add(finishCount + "");
            row.add(stats.get("unFinishCount") + "");
            row.add(stats.get("timeOutCount") + "");
            row.add((totalCount == 0 ? 0 : String.format("%.0f", finishCount * 100.0 / totalCount)) + "%");
            row.add(stats.get("highIncidenceCount") + "");

            int num = 0;
            List<Map<String, Object>> countData = (List<Map<String, Object>>) histogram.get("data");
            for (Map<String, Object> count : countData) {
                row.add(count.get("categoryName") + "/" + count.get("count"));
                num++;
            }
            while (num < 10) {
                row.add("--/--");
                num++;
            }

            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("街区名称");
        rowTopName.add("案卷总数");
        rowTopName.add("已处置案卷总数 ");
        rowTopName.add("未处置案卷总数");
        rowTopName.add("超时案卷总数");
        rowTopName.add("处置完成率");
        rowTopName.add("高发案卷总数");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");
        rowTopName.add("高发案卷类型/数量");

        String excelName = "案卷统计_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + excelName);
        response.setHeader("Export-Excel", excelName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();

//        String fileName = "案卷统计_" + StringUtil.getExportTime();
//
//        ServletOutputStream out = response.getOutputStream();
//        String userAgent = request.getHeader("user-agent");
//        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//        } else {//非IE内核
//            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
//        }
//
//        fileName += ".xlsx";
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/download");
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);
//
//        out.flush();
//        out.close();
    }

    @Override
    public List<Map<String, String>> score(String time, String unitId) {
//        time = time + "/01 00:00:00";
//        Date date = Objects.requireNonNull(StringUtil.getTime(time));
//        long startTime = date.getTime();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.MONTH, 1);
//        long endTime = calendar.getTime().getTime();
//        Unit unit = unitService.findById("b625f135499741c8b320c8177e99c877");
//
//        List<String> unitIds = unitService.findAllByPid(unit.getId());
//        unitIds.add(unit.getId());
//
//        long l = System.currentTimeMillis();
//
//        Map<String, String> data = new HashMap<>();
//        for (int i = 0; i < 200; i++) {
//            final Integer b = i;
//            ThreadPool.threadPool.execute(() -> {
//                long l0 = System.currentTimeMillis() - l;
//
//                //总数
//                long totalCount = workService.count(startTime, endTime, unitIds);
//                data.put("totalCount", totalCount + "");
//                long l1 = System.currentTimeMillis() - l;
//
//                //结束案卷总数
//                long finishCount = workService.finishCount(startTime, endTime, unitIds);
//                data.put("finishCount", finishCount + "");
//                long l2 = System.currentTimeMillis() - l;
//
//                //未处置案卷总数
//                data.put("unFinishCount", workService.unFinishCount(startTime, endTime, unitIds) + "");
//                long l3 = System.currentTimeMillis() - l;
//
//                //超时案卷
//                data.put("timeOutCount", workService.timeOutCount(startTime, endTime, unitIds) + "");
//                data.put("finishRate", (totalCount == 0 ? 0 : finishCount * 10000 / totalCount / 100.0) + "%");
//                long l4 = System.currentTimeMillis() - l;
//
//                //按期处置案卷总数
//                long onTimeCount = workService.onTimeCount(startTime, endTime, unitIds);
//                data.put("onTimeRate", (totalCount == 0 ? 0 : onTimeCount * 10000 / totalCount / 100.0) + "%");
//                long l5 = System.currentTimeMillis() - l;
//
//                //返工数量
//                long returnCount = logService.returnCount(startTime, endTime, unitIds);
//                data.put("returnRate", (totalCount == 0 ? 0 : returnCount * 10000 / totalCount / 100.0) + "%");
//                long l6 = System.currentTimeMillis() - l;
//                //在线率
//                double average = dao.findByUnitId(startTime, endTime, unit.getId()).stream().mapToDouble(Stats::getUserOnline).summaryStatistics().getAverage();
//                data.put("onlineRate", average * 100 + "%");
//                long l7 = System.currentTimeMillis() - l;
//
//                data.put("score",
//                        (totalCount == 0 ? 0 : onTimeCount * 10000 / totalCount / 100.0 * 0.4) +
//                                (totalCount == 0 ? 0 : (finishCount * 10000) / totalCount / 100.0 * 0.3) +
//                                (totalCount == 0 ? 0 : (1 - (returnCount * 10000) / totalCount / 10000.0) * 100 * 0.2) +
//                                ((int) average * 10000) / 100.0 * 0.1 + ""
//                );
//
//                data.put("unitId", unit.getId());
//                data.put("unitName", unit.getName());
//                System.out.println("线程 " + b
//                        + "    开始 " + l0
//                        + "    总数 " + l1
//                        + "    结束案卷总数 " + l2
//                        + "    未处置案卷总数 " + l3
//                        + "    超时案卷 " + l4
//                        + "    按期处置案卷总数 " + l5
//                        + "    返工数量 " + l6
//                        + "    在线率 " + l7
//                );
//            });
//        }
//        return data;
        time = time + "/01 00:00:00";
        Date date = Objects.requireNonNull(StringUtil.getTime(time));
        long startTime = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        long endTime = calendar.getTime().getTime();

        List<Unit> units = new ArrayList<>();
        if (EmptyUtil.isEmpty(unitId)) {
            units = unitService.findByPid(unitService.findByPid(null).get(0).getId());
        } else {
            units.add(unitService.findById(unitId));
        }

        List<CompletableFuture<Map<String, String>>> collect = units.stream().map(unit -> CompletableFuture
                .supplyAsync(() -> {
                    List<String> unitIds = unitService.findAllByPid(unit.getId());
                    unitIds.add(unit.getId());

                    Map<String, String> data = new HashMap<>();
                    //总数
                    long totalCount = workService.count(startTime, endTime, unitIds);
                    data.put("totalCount", totalCount + "");

                    //结束案卷总数
                    long finishCount = workService.finishCount(startTime, endTime, unitIds);
                    data.put("finishCount", finishCount + "");

                    //未处置案卷总数
                    data.put("unFinishCount", workService.unFinishCount(startTime, endTime, unitIds) + "");

                    //完成率
                    data.put("finishRate", (totalCount == 0 ? 0 : finishCount * 10000 / totalCount / 100.0) + "%");

                    //按期处置案卷总数
                    long onTimeCount = workService.onTimeCount(startTime, endTime, unitIds);
                    data.put("onTimeRate", (totalCount == 0 ? 0 : onTimeCount * 10000 / totalCount / 100.0) + "%");

                    //返工数量
                    long returnCount = logService.returnCount(startTime, endTime, unitIds);
                    data.put("returnRate", (totalCount == 0 ? 0 : returnCount * 10000 / totalCount / 100.0) + "%");

                    //在线率
                    double average = dao.findByUnitId(startTime, endTime, unit.getId()).stream().mapToDouble(Stats::getUserOnline).summaryStatistics().getAverage();
                    data.put("onlineRate", String.format("%.0f", average * 100) + "%");

                    double d = (totalCount == 0 ? 0 : onTimeCount * 1.0 / totalCount * 100 * 0.4) +
                            (totalCount == 0 ? 0 : (finishCount * 1.0) / totalCount * 100.0 * 0.3) +
                            (totalCount == 0 ? 0 : (1 - returnCount * 1.0 / totalCount) * 100 * 0.2) +
                            ((int) average) * 100.0 * 0.1;
                    BigDecimal b = new BigDecimal(d);
                    double doubleValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    data.put("score", doubleValue + "");

                    data.put("unitName", unit.getName());
                    data.put("unitId", unit.getId());
                    return data;
                }, ThreadPool.threadPool).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
                    List<String> unitIds = unitService.findAllByPid(unit.getId());
                    unitIds.add(unit.getId());

                    //超时案卷
                    return workService.timeOutCount(startTime, endTime, unitIds) + "";
                }, ThreadPool.threadPool), (data, timeOutCount) -> {
                    data.put("timeOutCount", timeOutCount);
                    return data;
                }, ThreadPool.threadPool)
        ).collect(Collectors.toList());
        List<Map<String, String>> list = new ArrayList<>();
        for (CompletableFuture<Map<String, String>> item : collect) {
            try {
                list.add(item.get());
            } catch (Exception e) {
            }
        }
        return list;
    }

    @Override
    public void exportScore(String time, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Map<String, String>> data = score(time, null);
        //排序
        List<Map<String, String>> sortData = data.stream().sorted(Comparator.comparingDouble(item -> Double.parseDouble(item.get("score")))).collect(Collectors.toList());
        Collections.reverse(sortData);

        Map<String, Integer> sort = new HashMap<>();
        int num = 0;
        String score = null;
        for (int i = 0; i < sortData.size(); i++) {
            Map<String, String> item = sortData.get(i);
            String itemScore = item.getOrDefault("score", "");
            if (!itemScore.equals(score)) {
                num++;
            }
            score = itemScore;
            sort.put(item.get("unitId"), num);
        }

        Vector<Vector<String>> rowName = new Vector<>();
        for (Map<String, String> item : data) {
            Vector<String> row = new Vector<>();

            row.add(item.get("unitName"));
            row.add(time);
            row.add(item.get("totalCount"));
            row.add(item.get("finishCount"));
            row.add(item.get("unFinishCount"));
            row.add(item.get("timeOutCount"));
            row.add(item.get("finishRate"));
            row.add(item.get("onlineRate"));
            row.add(item.get("returnRate"));
            row.add(item.get("onlineRate"));
            row.add(item.get("score"));

            row.add(sort.get(item.get("unitId")) + "");

            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("街区");
        rowTopName.add("月份");
        rowTopName.add("案卷总数");
        rowTopName.add("已处置案卷总数 ");
        rowTopName.add("未处置案卷总数");
        rowTopName.add("超时案卷总数");
        rowTopName.add("处置完成率");
        rowTopName.add("按时处置率");
        rowTopName.add("返工率");
        rowTopName.add("出勤率");
        rowTopName.add("考评分值");
        rowTopName.add("排名");

        String excelName = "综合评价_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + excelName);
        response.setHeader("Export-Excel", excelName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();
    }
}
