package cn.bytecloud.smartCommunity.log.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.log.dao.LogDao;
import cn.bytecloud.smartCommunity.log.dao.LogRepository;
import cn.bytecloud.smartCommunity.log.entity.Log;
import cn.bytecloud.smartCommunity.log.entity.LogItem;
import cn.bytecloud.smartCommunity.log.entity.LogType;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.path.service.PathService;
import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.record.service.RecordService;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.service.TodoService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDao dao;
    @Autowired
    private LogRepository repository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private WorkService workService;

    @Autowired
    private PathService pathService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Override
    public Log save(LogType logType, String pathId, String workId, String todoId, String desc, Long time, TimeType timeType, List<String> userIds, List<String> todoIds,
                    long createTime) {
        return save(logType, pathId, workId, todoId, desc, time, timeType, userIds, todoIds, createTime, false);
    }

    @Override
    public Log save(LogType logType, String pathId, String workId, String todoId, String desc, Long time, TimeType timeType, List<String> userIds, List<String> todoIds,
                    long createTime, Boolean endFlag) {
        Log log = repository.findOneByWorkId(workId);
        if (log == null) {
            log = new Log();
            log.setUnitId(workService.findById(workId).getUnitId());
            log.setWorkId(workId);
            log.setCreateTime(System.currentTimeMillis());
        }
        LogItem item = new LogItem();
        item.setTodoId(todoId);
        item.setLogType(logType);
        item.setDesc(desc);
        item.setPathId(pathId);
        item.setTime(time);
        item.setTimeType(timeType);
        item.setUserIds(userIds);
        item.setTodoIds(todoIds);
        item.setCreateTime(createTime);
        item.setCreatorId(UserUtil.getUserId());
        log.getLogItems().addLast(item);
        log.setHandlerCount(log.getLogItems().size());
        log.setEndFlag(endFlag);
        log.setHandlerTime(System.currentTimeMillis());
        repository.save(log);
        return log;
    }

    @Override
    public void delByWorkId(String workId) {
        dao.delByWorkId(workId);
    }

    @Override
    public Object list(String id, Integer type) throws ByteException {
        Work work;
        if (type == 1) {
            Todo todo = todoService.findById(id).orElseThrow(() -> new ByteException("待办失效"));
            work = workService.findById(todo.getWorkId());
        } else {
            work = workService.findById(id);
        }
        if (work == null) {
            throw new ByteException("无效数据");
        }
        int count = work.getCount();

        String userId = UserUtil.getUserId();
        List<Map<String, Object>> list = new ArrayList<>();
        LinkedList<LogItem> logItems = repository.findOneByWorkId(work.getId()).getLogItems();

        for (int j = 0; j < logItems.size(); j++) {
            LogItem logItem = logItems.get(j);
            Map<String, Object> map = new HashedMap<>();
            LogType logType = logItem.getLogType();
            if (logType == LogType.CREATE || logType == LogType.END || logType == LogType.APIINSPECTION || logType == LogType.WITHDRAW_END) {
                map.put("title", logType.getEnumValue());
            } else {
                Path path = pathService.findById(logItem.getPathId());
                map.put("title", logItem.getLogType() == LogType.WITHDRAW ? "撤回--[" + path.getDesc() + "]" : path.getDesc());
            }
            User u = userService.findById(logItem.getCreatorId());
            map.put("username", u.getName());
            map.put("imagePath", u.getImagePath());
            map.put("createTime", StringUtil.getTime(new Date(logItem.getCreateTime())));
            map.put("desc", logItem.getDesc());
            if (logItem.getTime() != null && logItem.getTimeType() != null) {
                map.put("time", logItem.getTime());
                map.put("timeType", logItem.getTimeType());
            }
            Boolean flag = null;
            if (work.isEndFlag() && logType != LogType.END) {
                flag = false;
            } else if (work.isEndFlag() && logType == LogType.END) {
                flag = logItem.getCreatorId().equals(userId) && userId.equals(work.getFinishUserid()) && logItem.getCreateTime() == work.getFinishTime();
            } else {
                Optional<Record> record = recordService.findByWorkIdAndTodoId(work.getId(), logItem.getTodoId());

                flag = record.isPresent() && logItem.getCreatorId().equals(userId) && record.get().getCreateTime() == logItem.getCreateTime();
            }

            if (flag) {
                map.put("todoId", logItem.getTodoId());
                if (EmptyUtil.isNotEmpty(logItem.getPathId())) {
                    map.put("pathId", logItem.getPathId());
                }
            }
            if (count != 0) {
                flag = false;
            }
            if (logType == LogType.END) {
                count--;
            }

            map.put("withdrawFlag", flag);

            boolean endFlag = logItem.getUserIds() == null || logItem.getUserIds().size() == 0;
            if (!endFlag) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < logItem.getUserIds().size(); i++) {
                    User user = userService.findById(logItem.getUserIds().get(i));
                    if (user != null) {
                        if (sb.length() != 0) {
                            sb.append(",");
                        }
                        sb.append(user.getName());
                    }
                }
                map.put("nextHandlers", sb.toString());

                boolean finishFlag = true;

                for (String todoId : logItem.getTodoIds()) {
                    if (!todoService.findById(todoId).isPresent()) {
                        finishFlag = false;
                        break;
                    }
                }
                map.put("finishFlag", finishFlag);
            }
            map.put("endflag", endFlag);


            list.add(map);
        }

        return list;
    }

    @Override
    public long onceCount(long startTime, long endTime, List<String> unitId) {
        return dao.onceCount(startTime, endTime, unitId);
    }

    @Override
    public long returnCount(long startTime, long endTime, List<String> unitIds) {
        return dao.returnCount(startTime, endTime, unitIds);
    }

    @Override
    public Log findOneByWorkId(String workId) {
        return repository.findOneByWorkId(workId);
    }
}
