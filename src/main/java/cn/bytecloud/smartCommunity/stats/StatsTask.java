package cn.bytecloud.smartCommunity.stats;

import cn.bytecloud.smartCommunity.stats.dao.StatsRepository;
import cn.bytecloud.smartCommunity.stats.entity.Stats;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.userAddress.service.UserAddressService;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatsTask {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @Autowired
    private StatsRepository repository;

    @Autowired
    private UnitService unitService;

    /**
     * 统计每个街区的每天在线率
     */
    @Scheduled(cron = "0 0 21 ? * MON-FRI")
    public void task() {
        long currentTime = System.currentTimeMillis();
        for (Unit unit : unitService.findAll()) {
            List<String> ids = unitService.findAllByPid(unit.getId());
            ids.add(unit.getId());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(currentTime));
            calendar.add(Calendar.HOUR_OF_DAY, -21);
            List<User> users = userService.findByUserTypeAndUnitIds(UserType.WORKER, ids);

            int userCount = users.size();
            long fateCount = addressService.onlineCountByIds(calendar.getTime().getTime(), users.stream().map(User::getId).collect(Collectors.toSet()));

            Stats stats = new Stats();
            stats.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            stats.setMonth(calendar.get(Calendar.MONTH) + 1);
            stats.setYear(calendar.get(Calendar.YEAR));
            stats.setUserOnline(userCount == 0 ? 0.0 : fateCount * 10000 / userCount / 10000.0);

            stats.setId(UUIDUtil.getUUID());
            stats.setCreateTime(currentTime);
            stats.setUnitId(unit.getId());
            repository.save(stats);
        }
    }
}
