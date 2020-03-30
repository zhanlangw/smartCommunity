package cn.bytecloud.smartCommunity.work.service;

import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.address.service.AddressService;
import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.bigCategory.service.BigCategoryService;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.blacklist.service.BlacklistService;
import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.file.service.FileService;
import cn.bytecloud.smartCommunity.log.entity.Log;
import cn.bytecloud.smartCommunity.log.entity.LogItem;
import cn.bytecloud.smartCommunity.log.entity.LogType;
import cn.bytecloud.smartCommunity.log.service.LogService;
import cn.bytecloud.smartCommunity.node.entity.HandlerType;
import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.node.entity.NodeType;
import cn.bytecloud.smartCommunity.node.service.NodeService;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.path.service.PathService;
import cn.bytecloud.smartCommunity.process.service.ProcessService;
import cn.bytecloud.smartCommunity.record.entity.Record;
import cn.bytecloud.smartCommunity.record.entity.RecordType;
import cn.bytecloud.smartCommunity.record.service.RecordService;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.stats.dto.AppHistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.HistogramStatsDto;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.todo.entity.Todo;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.todo.service.TodoService;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.*;
import cn.bytecloud.smartCommunity.work.dao.WorkDao;
import cn.bytecloud.smartCommunity.work.dao.WorkRepository;
import cn.bytecloud.smartCommunity.work.dto.*;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WIN10
 */
@Slf4j
@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private LogService logService;

    @Autowired
    private PathService pathService;

    @Autowired
    private SmallCategoryService smallCategoryService;

    @Autowired
    private BigCategoryService bigCategoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BasisService basisService;

    @Autowired
    private FileService fileService;

    @Autowired
    private WorkDao dao;

    @Autowired
    private WorkRepository repository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private RecordService recordService;


    @Autowired
    private ProcessService processService;

    @Autowired
    private UnitService unitService;

    public static void main(String[] args) {

        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String s = outFormat.format(new Date());
        System.out.println(s);
    }

    @Override
    public List<Work> findByProcessId(String id) {
        return repository.findByProcessId(id);
    }

    /**
     * 添加
     *
     * @param dto
     */
    @Override
    public void add(@Validated AddWorkDto dto) throws ByteException {
        Work work = dto.toData();

        Work old = dao.findAiInspection(work.getBlacklistId(), work
                .getDeviceId(), systemConstant.inspectionTime);

        //没有审核给管理员审核 (摄像机抓拍)
        if (old != null) {
            work.setId(old.getId());
            work.setSource(WorkSource.AIINSPECTION);
            work.setCount(old.getCount()+1);
            work.setReaderIds(old.getReaderIds());
            work.setAcceptFlag(true);
            work.setCreateTime(old.getCreateTime());
            work.setCreateDay(old.getCreateDay());
            work.setCreateMonth(old.getCreateMonth());
            work.setCreateYear(old.getCreateYear());
            work.setCreateTime(System.currentTimeMillis());
            work.setCreatorId(old.getCreatorId());
            startProcess(work, PathAttribute.APIINSPECTION, LogType.APIINSPECTION);
        } else if (!work.isAcceptFlag()) {
            dao.save(work);
        } else {
            startProcess(work, PathAttribute.CREATE, LogType.CREATE);
        }
//        return EntityUtil.entityToDto(work, WorkItemDto.class);
    }

    /**
     * 开始流程
     *
     * @param work
     * @throws ByteException
     */
    private void startProcess(Work work, PathAttribute attribute, LogType logType) throws ByteException {
        work.getReaderIds().add(UserUtil.getUserId());

        TodoType todoType = TodoType.TODO;

        //获取流程开始节点,并且设置为当前流程所处环节
        Node node;
        try {
            node = nodeService.findByProcessIdAndNodeType(work.getProcessId(), NodeType.START).get(0);
        } catch (Exception e) {
            throw new ByteException("流程对应开始环节有误!");
        }
        work.setEndTime(getEndTime(work));

        work.setUpdateTime(System.currentTimeMillis());
        work.setNum(createNum(work));
        List<String> headlers;
        if (attribute == PathAttribute.APIINSPECTION) {
            headlers = new ArrayList<>();
            LinkedList<LogItem> logItems = logService.findOneByWorkId(work.getId()).getLogItems();
            logItems.removeLast();
            headlers.add(logItems.getLast().getCreatorId());
        } else {
            headlers = getHeadlers(node, work, attribute, null, null);
        }
        dao.save(work);
        Todo todo = todoService.addTodo(work, todoType, headlers, null, null, attribute, node.getId());

        List<String> todoIds = new ArrayList<>();
        todoIds.add(todo.getId());

        logService.save(logType, null, work.getId(), null, work.getDesc(), null, null, headlers, todoIds, System.currentTimeMillis());
    }

    /**
     * //结束时间
     *
     * @param work
     * @return
     */
    private long getEndTime(Work work) {
        SmallCategory category = smallCategoryService.findById(work.getTypeId());
        long endTime = category.getTime() * 3600 * 1000;
        endTime = category.getTimeType() == TimeType.DAY ? endTime * 24 : endTime;
        return System.currentTimeMillis() + endTime;
    }

    /**
     * 流程扭转
     *
     * @param work
     * @param dto
     * @return
     * @throws ByteException
     */
    private void handlerWork(Work work, Path afterPath, Todo todo, SubmitDto dto) throws ByteException {
        List<String> headlers = new ArrayList<>();
        List<String> todoIds = new ArrayList<>();
        boolean endFlag = false;
        LogType logType = LogType.ORDINATY;
        //是否是处理申请延时操作
        boolean flag = false;
        //选择路径id
        String afterPathId = afterPath.getId();
        //选择路径属性
        PathAttribute attribute = afterPath.getAttribute();
        //获取代办类型
        TodoType todoType = getTodoType(afterPath);
        //是否是处理申请延时操作
        if (todo.getType() == TodoType.DELAY) {
            flag = true;
        }
        if (todoType == TodoType.DELAY) {
            if (work.getDelayFlag()) {
                throw new ByteException("只能申请一次延时!");
            } else {
                work.setDelayFlag(true);
            }
        }
        //流程提交后到达环节
        Node node = nodeService.findById(afterPath.getEndNodeId());

        work.setUpdateTime(System.currentTimeMillis());

        if (node.getType() != NodeType.END && flag) {
            throw new ByteException("处理申请延时后必须是结束环节");
        }

        long createTime = System.currentTimeMillis();
        //处理流程前先生成记录,用于撤回
        recordService.save(work, todo, afterPath, node, createTime);

        //是否是结束环节
        if (node.getType() != NodeType.END) {
            //获取代办人员
            headlers = getHeadlers(node, work, attribute, dto.getUserIds(), dto.getUnitIds());
//            work.setHandlerIds(headlers.stream().map(User::getId).collect(Collectors.toList()));

            //退件从新指定案卷时间
            if (todo.getType() == TodoType.RETURN && attribute == PathAttribute.AGREE) {
                recordService.delByWorkId(work.getId());
                work.setEndTime(getEndTime(work));
                todoService.delByWorkId(work.getId());
                work.setReturnFlag(false);
                work.setDelayFlag(false);
            }
            if (attribute == PathAttribute.RETURN) {
                if (work.getReturnFlag() && !UserUtil.getUser().getUsername().equals(SystemConstant.INIT_ROOT_USERNAME)) {
                    throw new ByteException("只能退件一次");
                }
                work.setReturnFlag(true);
            }
            if (attribute == PathAttribute.REVIEW) {
                work.setAfterImagePaths(dto.getAfterImagePaths());
                work.setAfterVideoPaths(dto.getAfterVideoPaths());
                work.setHandleDesc(dto.getDesc());
            }

            //添加代办
            todoIds.add(todoService.addTodo(work, todoType, headlers, dto, afterPathId, attribute, node.getId()).getId());

            //发起退件或者协助时,保存发起前的代办人员信息
            if (todoType == TodoType.RETURN || attribute == PathAttribute.ADDUSER) {
                work.setReturnuserIds(todo.getHandlerIds());
            }
        } else {
            //支线,申请延时结束
            if (flag) {
                if (attribute == PathAttribute.AGREE) {
                    TimeType timeType = todo.getTimeType();
                    int num = timeType == TimeType.DAY ? 24 : 1;
                    work.setEndTime(work.getEndTime() + todo.getTime() * 3600 * 1000 * num);
                    todoService.updateEndTime(work);
                    recordService.updateEndTime(work);
                } else if (attribute == PathAttribute.REFUSE) {
                    //拒绝不做操作
                } else {
                    throw new ByteException("处理申请延时操作路径只能是同意或者拒绝");
                }
            } else {
                logType = LogType.END;
                endFlag = true;
                //整个流程结束
                work.setEndFlag(true);
                work.setFinishTime(createTime);
                work.setFinishUserid(UserUtil.getUserId());
                work.setStatus(WorkStatus.getStatus(work.getEndTime(), basisService.findFirst(), work.getFinishTime()));
                //删除所有代办
                todoService.delByWorkId(work.getId());
                ask(work, 1);

            }
        }

        work.getReaderIds().add(UserUtil.getUserId());
        work = dao.save(work);


        if (afterPath.getAttribute() != PathAttribute.DELAY) {
            //代办转已经办
            todoService.delById(todo.getId());
        }

        //日志
        logService.save(logType, afterPath.getId(), work.getId(), todo.getId(), dto.getDesc(), dto.getTime(), dto.getTimeType(), headlers, todoIds, createTime, endFlag);

//        //放入缓存中
//        toCache(work);

    }

//    /**
//     * 加入缓存
//     *
//     * @param work
//     */
//    private void toCache(Work work) {
//        ThreadPool.threadPool.execute(() -> {
//            WorkCache workCache = new WorkCache();
//            workCache.init(work);
//            redisTemplate.opsForValue().set(workCache.getId(), workCache);
//            redisTemplate.expire(work.getId(), 30, TimeUnit.DAYS);
//        });
//    }

    /**
     * 获取代办人员
     *
     * @param node
     * @param work
     * @param attribute
     * @param userIds
     * @return
     */
    private List<String> getHeadlers(Node node, Work work, PathAttribute attribute, List<String> userIds, List<String> unitIds) throws ByteException {
        List<String> data;

        //拒绝退件的办理人为发起退件的人
        if (attribute == PathAttribute.RETURN_REFUSE) {
            return work.getReturnuserIds();
        }

        //拒绝协助提交
        if (attribute == PathAttribute.REFUSE && node.getHandlerType() == HandlerType.CUSTOMIZE && EmptyUtil.isEmpty(userIds) && EmptyUtil.isEmpty(unitIds)) {
            return work.getReturnuserIds();
        }

        List<User> users;
        //获取办理人
        HandlerType handlerType = node.getHandlerType();
        if (handlerType == HandlerType.WORKER) {
            users = userService.findAllByUnitIdAndUserType(work.getUnitId(), UserType.WORKER);
            data = users.stream().map(BaseEntity::getId).collect(Collectors.toList());
        } else if (handlerType == HandlerType.ROOT) {
            users = userService.findByUserType(UserType.ADMIN);
            data = users.stream().map(BaseEntity::getId).collect(Collectors.toList());
        } else {
            Set<String> set = new HashSet<>(userIds);
            unitIds.forEach(unitId -> {
                set.addAll(userService.findAllByUnitId(unitId).stream().map(User::getId).collect(Collectors.toSet()));
            });
            data = new ArrayList<>(set);
        }
        if (data.size() == 0) {
            throw new ByteException("下一办理人为空");
        }
        return data;

    }

    /**
     * 根据路径获取代办类型
     *
     * @param afterPath
     * @return
     */
    private TodoType getTodoType(Path afterPath) {
        TodoType todoType;
        Integer type = afterPath.getAttribute().getEnumType();
        switch (type) {
            case 1:
                todoType = TodoType.DELAY;
                break;
            case 3:
                todoType = TodoType.RETURN;
                break;
            case 4:
                todoType = TodoType.REVIEW;
                break;
            default:
                todoType = TodoType.TODO;
                break;
        }
        return todoType;
    }

    /**
     * 生成案卷编号
     *
     * @param work
     * @return
     */
    private synchronized String createNum(Work work) {
        SmallCategory smallCategory = smallCategoryService.findById(work.getTypeId());
        BigCategory bigCategory = bigCategoryService.findById(smallCategory.getBigCategoryId());
        Unit unit = unitService.findById(work.getUnitId());

        //发送短信
        ThreadPool.threadPool.execute(() -> sendSms(unit, smallCategory, work));

        StringBuilder num = new StringBuilder(unit.getAbbre() + bigCategory.getAbbre()
                + smallCategory.getAbbre() + StringUtil.getStatsTime(new Date()));

        Optional<Work> workOptional = dao.findLast();
        if (workOptional.isPresent()) {
            String n = workOptional.get().getNum();
            int number = Integer.parseInt(n.substring(n.length() - 4));
            number += 1;
            int size = 0;
            int a = 1;
            while (number / a > 0) {
                size++;
                a *= 10;
            }
            if (size < 4) {
                for (int i = 0; i < 4 - size; i++) {
                    num.append("0");
                }
            }
            num.append(number);
        } else {
            num.append("0001");
        }
        return num.toString();
    }

    /**
     * 发送短信
     *
     * @param unit
     * @param smallCategory
     * @param work
     */
    private void sendSms(Unit unit, SmallCategory smallCategory, Work work) {
        if (smallCategory.getWorkType() == WorkType.URGENT) {
            String blacklistId = work.getBlacklistId();
            String name = EmptyUtil.isEmpty(blacklistId) ? "" : "-" + blacklistService.findById(blacklistId).getName();
            for (String telephone : unit.getTelephone().split(",")) {
                SmsUtil.sendSms(telephone, "紧急通知:" + work.getAddress() + "-" + smallCategory.getName() + name);
            }
        }
    }

//    /**
//     * 从缓存中取出案卷
//     *
//     * @param id
//     */
//    private WorkCache getCache(String id) {
//        WorkCache workCache = (WorkCache) redisTemplate.opsForValue().get(id);
//        if (workCache == null) {
//            workCache = new WorkCache();
//            workCache.init(repository.findOne(id));
//            redisTemplate.opsForValue().set(workCache.getId(), workCache);
//            redisTemplate.expire(id, 30, TimeUnit.DAYS);
//        }
//        return workCache;
//    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public void del(String ids) {
        for (String id : ids.split(",")) {
            Work work = repository.findOne(id);
            repository.delete(id);

            //删除代办
            todoService.delByWorkId(id);

            // 删除已经办理,日志
            logService.delByWorkId(id);

            //删除记录
            recordService.delByWorkId(id);

            //删除文件
            List<String> filePaths = new ArrayList<>();
            filePaths.addAll(work.getBeforeImagePaths());
            filePaths.addAll(work.getBeforeVideoPaths());
            filePaths.addAll(work.getAfterImagePaths());
            filePaths.addAll(work.getAfterVideoPaths());
            filePaths.forEach(path -> {
                try {
                    fileService.deleteFile(path);
                } catch (ByteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 详情
     *
     * @param id
     * @param type
     * @return
     */
    @Override
    public WorkItemDto item(String id, Integer type) throws ByteException {
        if (type == 1) {
            Todo todo = todoService.findById(id).orElseThrow(() -> new ByteException("待办失效"));
            Work work = repository.findOne(todo.getWorkId());
            WorkItemDto workItemDto = EntityUtil.entityToDto(work, WorkItemDto.class);
            workItemDto.setTodoId(todo.getId());
            workItemDto.setCurrNodeId(todo.getCurrNodeId());
            workItemDto.setType(type);
            workItemDto.setId(todo.getId());
            workItemDto.setWorkId(work.getId());
            workItemDto.setCurrentTime(work.isEndFlag() ? work.getFinishTime() : System.currentTimeMillis());
            workItemDto.setTodoType(todo.getType());
            return workItemDto;
        } else {
            Work work = repository.findOne(id);
            WorkItemDto workItemDto = EntityUtil.entityToDto(work, WorkItemDto.class);
            workItemDto.setType(type);
            workItemDto.setCurrentTime(work.isEndFlag() ? work.getFinishTime() : System.currentTimeMillis());
            workItemDto.setWorkId(work.getId());
            return workItemDto;
        }
    }

    /**
     * 获取流程可选路径
     *
     * @param id
     * @return
     */
    @Override
    public Object getPaths(String id) throws ByteException {
        List<Map<String, Object>> data = new ArrayList<>();
        Todo todo = todoService.findById(id).orElseThrow(() -> new ByteException("待办失效"));
        Work work = repository.findOne(todo.getWorkId());
        if (!work.isAcceptFlag()) {
            throw new ByteException("该案卷没有受理!");
        }
        if (work.isEndFlag()) {
            throw new ByteException("案卷已经结束");
        }
//        boolean flag = todoService.findByWorkId(work.getId()).stream().anyMatch(item -> item.getType() == TodoType.DELAY);
        for (Path path : pathService.findPathByNodeId(todo.getCurrNodeId(), work.getProcessId())) {
            PathAttribute attribute = path.getAttribute();
            if (work.getDelayFlag() && attribute == PathAttribute.DELAY) {
                continue;
            }
            if (work.getReturnFlag() && attribute == PathAttribute.RETURN) {
                continue;
            }
            Map<String, Object> map = new HashedMap<>();
            map.put("id", path.getId());
            map.put("name", path.getName());
            map.put("type", attribute);
            data.add(map);
        }
        return data;
    }

    /**
     * 受理
     *
     * @param flag
     * @param id
     * @throws ByteException
     */
    @Override
    public void accept(boolean flag, String id) throws ByteException {
        User user = UserUtil.getUser();
        if (user.getUserType() != UserType.ROOT && user.getUserType() != UserType.ADMIN) {
            throw new ByteException("权限不足");
        }
        Work work = repository.findOne(id);
        if (work.isAcceptFlag()) {
            throw new ByteException("该案卷不需要再次受理!");
        }
        if (work.isEndFlag()) {
            throw new ByteException("案卷已经结束");
        }

        //通过审核,进入流程
        if (flag) {
            work.setAcceptFlag(true);
            startProcess(work, PathAttribute.CREATE, LogType.CREATE);

            //删除代办
            todoService.delById(id);
        } else {
            //忽略案卷,删除
            del(work.getId());
            ask(work, 0);
        }

    }

    public void ask(Work work, int accept) {
        ThreadPool.threadPool.execute(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json;charset=utf-8");
            headers.set("Accept", "application/json");
            String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.faceAsk;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("eventId", work.getEventId());
            jsonObject.put("refId", work.getBlacklistId());
            jsonObject.put("accept", accept);
            HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
            ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
            log.info("人脸数据库－反馈 ->  eventId:" + work.getEventId() + "  -> accept " + accept + "  ->  " + respose.toString());
            JSONObject object = JSONObject.parseObject(respose.getBody());
            if (object.getIntValue("status") != 0) {
                log.info("事件忽略失败 ->  eventId:" + work.getEventId() + "  -> accept " + accept + "  ->  " + object.getString("message"), object.getIntValue("status"));
            }
        });
    }

    /**
     * 根据案卷类型查询
     *
     * @param typeId
     * @return
     */
    @Override
    public long countByTypeId(String typeId) {
        return dao.countByTypeId(typeId);
    }

    /**
     * 提交
     */
    @Override
    public void submit(SubmitDto dto) throws ByteException {
        synchronized (dto.getId().intern()) {
            Path path = pathService.findById(dto.getPathId());
            Work work = repository.findOne(dto.getId());
            Todo todo = todoService.findById(dto.getTodoId()).orElseThrow(() -> new ByteException("待办失效"));
            if (path.getAttribute() == PathAttribute.RETURN && EmptyUtil.isEmpty(dto.getDesc())) {
                throw new ByteException("描述不能为空");
            } else if (path.getAttribute() == PathAttribute.DELAY) {
                if (dto.getTime() == null) {
                    throw new ByteException("超时时长不能为空");
                }
                if (dto.getTimeType() == null) {
                    throw new ByteException("时长单位不能为空");
                }
                if (EmptyUtil.isEmpty(dto.getDesc())) {
                    throw new ByteException("描述不能为空");
                }

            } else if (path.getAttribute() == PathAttribute.REVIEW) {
                if (EmptyUtil.isEmpty(dto.getDesc())) {
                    throw new ByteException("描述不能为空");
                }
                if (dto.getAfterImagePaths() == null || dto.getAfterImagePaths().size() == 0) {
                    throw new ByteException("处理后图片必须上传");
                }

            } else if (path.getAttribute() == PathAttribute.ADDUSER && ((dto.getUserIds() == null || dto.getUserIds().size() == 0) && (dto.getUnitIds() == null || dto
                    .getUnitIds().size() == 0))) {
                throw new ByteException("用户不能为空");
            }
            if (todo.getType() == TodoType.RETURN && path.getAttribute() == PathAttribute.AGREE && ((dto.getUserIds() == null && dto.getUserIds().size() == 0) && (dto
                    .getUnitIds() == null || dto.getUnitIds().size() == 0))) {
                throw new ByteException("用户不能为空");
            }

            if (work.isEndFlag()) {
                throw new ByteException("案卷已经结束");
            }
            if (!work.isAcceptFlag()) {
                throw new ByteException("该案卷没有受理!");
            }
            User user = UserUtil.getUser();
            if (user.getUserType() != UserType.ROOT && !todo.getHandlerIds().contains(user.getId())) {
                throw new ByteException("权限不足");
            }
            handlerWork(work, path, todo, dto);
        }
    }

    /**
     * 结束
     *
     * @param id
     * @throws ByteException
     */
    @Override
    public void end(String id) throws ByteException {
        Todo todo = todoService.findById(id).orElseThrow(() -> new ByteException("待办失效"));

        Work work = repository.findOne(todo.getWorkId());


        User user = UserUtil.getUser();
        if (user.getUserType() != UserType.ROOT && !todo.getHandlerIds().contains(user.getId())) {
            throw new ByteException("权限不足");
        }

        long createTime = System.currentTimeMillis();
        //记录
        recordService.saveEnd(work, todo, createTime);


        work.setEndFlag(true);
        work.getReaderIds().add(user.getId());
        work.setFinishTime(createTime);
        work.setFinishUserid(UserUtil.getUserId());
        work.setStatus(WorkStatus.getStatus(work.getEndTime(), basisService.findFirst(), work.getFinishTime()));
        repository.save(work);

        todoService.delByWorkId(work.getId());

        //日志
        Log log = logService.save(LogType.END, null, work.getId(), todo.getId(), null, null, null, null, null, createTime, true);
    }

    /**
     * 撤回
     *
     * @param workId
     * @param todoId
     * @throws ByteException
     */
    @Override
    public void withdraw(String workId, String todoId, String pathId) throws ByteException {
        synchronized (workId.intern()) {
            Work work = repository.findOne(workId);
            if (work.isEndFlag()) {
                work.setEndFlag(false);
                work.setFinishTime(0);
                repository.save(work);
            }

            Record record = recordService.findByWorkIdAndTodoId(work.getId(), todoId).orElseThrow(() -> new ByteException("不允许撤回"));
            List<Todo> data = record.getData();

            //测绘延时
            if (record.getType() == RecordType.DELAY) {
                if (record.getTime() != null && record.getTime() > 0) {
                    work.setEndTime(work.getEndTime() - record.getTime());
                    repository.save(work);
                    todoService.updateEndTime(work);
                    recordService.updateEndTime(work);
                }
                todoService.delByWorkIdAndType(work.getId(), TodoType.DELAY);
            } else if (record.getType() == RecordType.ORDINARY) {
                //正常撤回
                todoService.delByWorkIdAndNeType(work.getId(), TodoType.DELAY);
            } else if (record.getType() == RecordType.RETURN) {
                //测绘退件
                todoService.delByWorkId(work.getId());
            }

            List<Todo> list = new ArrayList<>();
            data.forEach(todo -> {
                Optional<Todo> old = todoService.findById(todo.getId());
                if (!old.isPresent()) {
                    todo.setId(UUIDUtil.getUUID());
                    list.add(todo);
                } else {
                    Todo oldTodo = old.get();
                    todoService.delById(oldTodo.getId());
                    oldTodo.setId(UUIDUtil.getUUID());
                    list.add(oldTodo);
                }
            });
            todoService.save(list);
            list.forEach(todo -> {
                try {
                    if (todo.getType() == TodoType.TODO) {
                        PushMsg.pushMsg("您有一条新的待办,请尽快办理!", todo.getHandlerIds(), null);
                    }
                } catch (Exception e) {
                    log.info("app消息推送失败!!    todoId::" + todo.getId());
                }
            });
            if (record.getType() == RecordType.END) {
                recordService.delByWorkId(record.getWorkId());
            } else {
                recordService.delete(record.getId());
            }
            List<String> todoIds = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            list.forEach(todo -> {
                todoIds.add(todo.getId());
                userIds.addAll(todo.getHandlerIds());
            });

            logService.save(EmptyUtil.isNotEmpty(pathId) ? LogType.WITHDRAW : LogType.WITHDRAW_END, pathId, workId, null, null, null, null, userIds, todoIds, System
                    .currentTimeMillis(), false);
        }
    }

    /**
     * 摄像机添加案卷
     *
     * @param dto
     * @throws ByteException
     */
    @Override
    public void add(AddWorkFromDeviceDto dto, boolean modelFlag) throws ByteException {
        AddWorkDto workDto = new AddWorkDto();

        String message = modelFlag ? "物体识别数据  :  " : "人脸识别数据  :  ";
        log.info(message + "黑名单id ->" + dto.getBlacklistId() + "  摄像机id ->" + dto.getDeviceId());

        if (null != repository.findFirstByBlacklistIdAndDeviceIdAndAcceptFlagAndEndFlag(dto.getBlacklistId(), dto.getDeviceId(), true, false)) {
            log.info("数据丢弃");
            return;
        }
        workDto.setSource(WorkSource.SYSTEM);
        String uuid = UUIDUtil.getUUID();
        String dataPath = "/" + uuid + ".jpg";
        String path = FileUtil.getProjectPath() + dataPath;

        log.info("文件保存路径++" + path);
        File saveFile = new File(path);
        if (saveFile.exists() && saveFile.isFile()) {
            saveFile.delete();
            saveFile = new File(path);
        }

        // 判断路径是否存在,如果不存在就创建文件路径
        if (!saveFile.getParentFile().exists()) {
            final boolean mkdirs = saveFile.getParentFile().mkdirs();
        }
        Base64Utils.Base64ToImage(dto.getImageBase64Str(), saveFile);

        //压缩图片
        int index = saveFile.getName().lastIndexOf(".");
        String compressionPath = FileUtil.getProjectPath() + "/" + saveFile.getName().substring(0, index) + "_compression.jpg";
        try {
            Thumbnails.of(new File(path)).scale(1f).outputQuality(0.15f).toFile(compressionPath);
            log.info("图片压缩成功,压缩后地址:" + compressionPath);
        } catch (Exception e) {
            try {
                byte[] b = new byte[1024];
                int n;
                InputStream inputStream = new FileInputStream(path);
                OutputStream outputStream = new FileOutputStream(compressionPath);
                while ((n = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, n);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e1) {
                log.info("图片压缩失败,原来文件:" + path);
            }
            log.info("图片压缩成功,压缩后地址:" + compressionPath);
        }
        workDto.getBeforeImagePaths().add("/uploads" + dataPath);

        //刷新同一摄像机同一案卷类型,没有被受理的案卷
        Work old = repository.findFirstByBlacklistIdAndDeviceIdAndAcceptFlag(dto.getBlacklistId(), dto.getDeviceId(), false);
        if (old != null) {
            //人脸
            if (!modelFlag) {
                for (String p : old.getBeforeImagePaths()) {
                    fileService.deleteFile(p);
                }
                old.setBeforeImagePaths(workDto.getBeforeImagePaths());
                old.setCreateTime(System.currentTimeMillis());
                old.setEventId(dto.getEventId());
            } else {
                //物体识别
                List<String> beforeImagePaths = old.getBeforeImagePaths();
                if (beforeImagePaths.size() >= 10) {
                    beforeImagePaths.remove(0);
                }
                beforeImagePaths.add(workDto.getBeforeImagePaths().get(0));
            }
            old.setUpdateTime(System.currentTimeMillis());
            repository.save(old);
            return;
        }


        Device device = deviceService.findById(dto.getDeviceId());
        Blacklist blacklist = blacklistService.findById(dto.getBlacklistId());
        Unit unit = unitService.findById(device.getUnitId());
        SmallCategory category = smallCategoryService.findById(blacklist.getTypeId());

//        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        String s = outFormat.format(new Date());

        String title = device.getAddress() + "-" + category.getName() + "-" + blacklist.getName();

        workDto.setTitle(title);
        workDto.setLatitude(device.getLatitude());
        workDto.setUnitId(unit.getId());
        workDto.setLongitude(device.getLongitude());
        workDto.setWorkType(category.getWorkType());
        workDto.setSource(WorkSource.SYSTEM);
        workDto.setAddress(device.getAddress());
        workDto.setTypeId(category.getId());
        workDto.setBlacklistId(dto.getBlacklistId());
        workDto.setDeviceId(dto.getDeviceId());
        workDto.setEventId(dto.getEventId());

        add(workDto);
    }

    @Override
    public Object todoList(WrokPageDto dto) {
        return dao.todoList(dto, basisService.findFirst());
    }

    @Override
    public void todoDel(String ids) {
        todoService.delByIds(ids);
    }

    @Override
    public void finishDel(String ids) {
        String userId = UserUtil.getUserId();
        dao.findByIds(Arrays.stream(ids.split(",")).collect(Collectors.toList())).forEach(work -> {
            work.getReaderIds().remove(userId);
            repository.save(work);
        });
    }

    @Override
    public Object acceptList(WrokPageDto dto) {
        return dao.acceptList(dto);
    }

    @Override
    public Object finishList(WrokPageDto dto) {
        return dao.finishList(dto);
    }

    @Override
    public Object historyList(WrokPageDto dto) {
        return dao.historyList(dto);
    }

    @Override
    public Object alarmList() {
        Map<String, String> types = new HashedMap<>();
        smallCategoryService.findByWorkType(WorkType.URGENT).forEach(category -> types.put(category.getId(), category.getName()));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Work work : dao.alarmList(types)) {
            if (EmptyUtil.isEmpty(work.getLatitude()) || EmptyUtil.isEmpty(work.getLongitude())) {
                continue;
            }
            Map<String, Object> map = new HashedMap<>();
            map.put("id", work.getId());
            map.put("source", work.getSource());
            map.put("categoryName", types.getOrDefault(work.getTypeId(), smallCategoryService.findById(work.getTypeId()).getName()));
            map.put("image", work.getBeforeImagePaths().size() > 0 ? work.getBeforeImagePaths().get(0) : "");
            map.put("longitude", work.getLongitude());
            map.put("latitude", work.getLatitude());
            map.put("createTime", StringUtil.getTime(new Date(work.getCreateTime())));
            data.add(map);
        }
        return data;
    }

    @Override
    public Work findById(String workId) {
        return repository.findOne(workId);
    }

    @Override
    public Object homeList() {
        return todoService.homeList();
    }

    @Override
    public Object gpsUnit(String latitude, String longitude) throws ByteException {
        Unit unit = unitService.findById(addressService.findUnitIdByLatitudeAndLongitude(latitude, longitude));
        Map<String, Object> map = new HashedMap<>();
        map.put("id", unit.getId());
        map.put("name", unit.getName());
        return map;
    }

    @Override
    public void updImage(UpdImageDto dto) throws ByteException {
        Work work = repository.findOne(dto.getWorkId());
        if (work == null) {
            throw new ByteException("案卷不存在!");
        }
        ArrayList<String> data = new ArrayList<>();
        data.addAll(work.getBeforeVideoPaths());
        data.addAll(work.getBeforeImagePaths());
        data.forEach(path -> {
            try {
                fileService.deleteFile(path);
            } catch (ByteException e) {
                e.printStackTrace();
            }
        });
        work.setBeforeVideoPaths(dto.getBeforeVideoPaths());
        work.setBeforeVideoPaths(dto.getBeforeVideoPaths());
        repository.save(work);
    }

    @Override
    public Object dataBroadcast(StatsDto dto) throws ByteException {
        Map<String, Long> map = new HashedMap<>();
        map.put("totalCount", dao.totalCount(dto));
        map.put("finishCount", dao.finishCount(dto));
        map.put("unFinishCount", dao.unFinishCount(dto));
        map.put("timeOutCount", dao.timeOutCount(dto));
        map.put("alarnCount", dao.alarnCount(dto, smallCategoryService.findByWorkType(WorkType.URGENT).stream().map(BaseEntity::getId).collect(Collectors.toList())));
        return map;
    }

    @Override
    public Object workStats(StatsDto dto) {
        List<Unit> units = unitService.findByPid(unitService.findByPid(null).get(0).getId());
        return dao.workStats(dto, units);
    }

    @Override
    public long count(long startTime, long endTime, List<String> unitId) {
        return dao.count(startTime, endTime, unitId);
    }

    @Override
    public long countByStatus(long startTime, long endTime, List<String> unitId, WorkStatus status) {
        return dao.countByStatus(startTime, endTime, unitId, status);
    }

    @Override
    public long finishCount(long startTime, long endTime, List<String> unitId) {
        return dao.finishCount(startTime, endTime, unitId);
    }

    @Override
    public Object trend(String unitId, int count) {
        Calendar start = Calendar.getInstance();
        start.add(Calendar.YEAR, -1 * count);
        long startTime = start.getTime().getTime();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, -1 * count + 1);
        long endTime = end.getTime().getTime();

        List<String> unitIds = unitService.findAllByPid(unitId);
        unitIds.add(unitId);

        //查找前6的高发案卷
        Map<String, Integer> categoryData = getMaxCategoryIds(unitIds, startTime, endTime, 6);

        List<Object> result = new ArrayList<>();
        categoryData.keySet().
                stream().
                map(categoryId -> CompletableFuture.supplyAsync(() -> dao.trendByTypeId(categoryId, startTime, endTime, unitIds), ThreadPool.threadPool)).
                map(item -> item.thenApplyAsync(list -> {
                    Map<String, List<HashMap>> data = new HashMap<>();
                    list.forEach((key, value) -> {
                        data.put(key, checkData(value, count));
                    });
                    return data;
                }, ThreadPool.threadPool)).
                collect(Collectors.toList()).forEach(item -> {
            try {
                result.add(item.get(1000 * 10, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                e.printStackTrace();
                log.info("统计失败！！");
            }
        });
        return result;
    }

    @Override
    public List<Work> map(StatsDto dto) {
        return dao.map(dto);
    }

    @Override
    public Object appHistogram(AppHistogramStatsDto dto) {
        long time = System.currentTimeMillis();
        List<CompletableFuture<Map<String, Map<String, Object>>>> resultFuture = dto.getUnit().stream().map(unit -> CompletableFuture.supplyAsync(() -> {
            List<String> unitIds = unitService.findAllByPid(unit.getId());
            unitIds.add(unit.getId());
            Map<String, Integer> categoryData = getMaxCategoryIds(unitIds, dto.getStartTime(), dto.getEndTime(), 5);

            Map<String, Map<String, Object>> data = new HashMap<>();
            Map<String, Object> unitMap = new HashMap<>();
            unitMap.put("id", unit.getId());
            unitMap.put("name", unit.getName());
            data.put("unit", unitMap);

            Map<String, Object> map = new HashMap<>();
            map.put("totalCount", dao.count(dto.getStartTime(), dto.getEndTime(), unitIds));

            map.put("finishCount", dao.finishCount(dto.getStartTime(), dto.getEndTime(), unitIds));

            map.put("unFinishCount", dao.unFinishCount(dto.getStartTime(), dto.getEndTime(), unitIds));

            map.put("highIncidenceCount", categoryData.values().stream().mapToInt(item -> item).sum());

            data.put("stats", map);
            return data;
        }, ThreadPool.threadPool).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            List<String> unitIds = unitService.findAllByPid(unit.getId());
            unitIds.add(unit.getId());
            return dao.timeOutCount(dto.getStartTime(), dto.getEndTime(), unitIds);
        }, ThreadPool.threadPool), (data, timeOutCOunt) -> {
            data.get("stats").put("timeOutCount", timeOutCOunt);
            return data;
        }, ThreadPool.threadPool)).collect(Collectors.toList());
        List<Map<String, Map<String, Object>>> result = new ArrayList<>();
        for (CompletableFuture<Map<String, Map<String, Object>>> future : resultFuture) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                log.info("统计失败");
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Map<String, Object>>> histogram(HistogramStatsDto dto) {
        long time = System.currentTimeMillis();
        List<CompletableFuture<Map<String, Map<String, Object>>>> resultFuture = dto.getUnit().stream().map(unit -> CompletableFuture.supplyAsync(() -> {
            List<String> unitIds = unitService.findAllByPid(unit.getId());
            unitIds.add(unit.getId());
            Map<String, Integer> categoryData = getMaxCategoryIds(unitIds, dto.getStartTime(), dto.getEndTime(), 10);
            Map<String, Object> data = new HashMap<>();
            Map<String, String> unitMap = new HashMap<>();
            unitMap.put("id", unit.getId());
            unitMap.put("name", unit.getName());
            data.put("unit", unitMap);

            List<Map<String, Object>> workData = new ArrayList<>();
            categoryData.forEach((key, value) -> {
                Map<String, Object> workCount = new HashMap<>();
                SmallCategory category = smallCategoryService.findById(key);
                workCount.put("categoryId", category.getId());
                workCount.put("categoryName", category.getName());
                workCount.put("count", value);
                workData.add(workCount);
            });
            data.put("data", workData);
            data.put("values", categoryData.values());

            return data;
        }, ThreadPool.threadPool).thenCombineAsync(
                CompletableFuture.supplyAsync(() -> {
                    List<String> unitIds = unitService.findAllByPid(unit.getId());
                    unitIds.add(unit.getId());

                    Map<String, Object> map = new HashMap<>();
                    map.put("totalCount", dao.count(dto.getStartTime(), dto.getEndTime(), unitIds));

                    map.put("finishCount", dao.finishCount(dto.getStartTime(), dto.getEndTime(), unitIds));

                    map.put("unFinishCount", dao.unFinishCount(dto.getStartTime(), dto.getEndTime(), unitIds));
                    return map;
                }, ThreadPool.threadPool), (histogram, stats) -> {
                    Collection<Integer> values = (Collection<Integer>) histogram.remove("values");
                    List<Integer> collect = values.stream().sorted(Comparator.comparingInt(count -> count)).collect(Collectors.toList());
                    long highIncidenceCount = 0;
                    int count = 0;
                    while (collect.size() > 0 && count != 5) {
                        highIncidenceCount += collect.remove(collect.size() - 1);
                        count++;
                    }
                    stats.put("highIncidenceCount", highIncidenceCount);

                    Map<String, Map<String, Object>> data = new HashMap<>();
                    data.put("stats", stats);
                    data.put("histogram", histogram);

                    return data;
                }, ThreadPool.threadPool
        ).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            List<String> unitIds = unitService.findAllByPid(unit.getId());
            unitIds.add(unit.getId());
            Long timeOutCount = dao.timeOutCount(dto.getStartTime(), dto.getEndTime(), unitIds);
            return timeOutCount;
        }, ThreadPool.threadPool), (data, timeOutCOunt) -> {
            data.get("stats").put("timeOutCount", timeOutCOunt);
            return data;
        }, ThreadPool.threadPool)).collect(Collectors.toList());
        List<Map<String, Map<String, Object>>> result = new ArrayList<>();
        for (CompletableFuture<Map<String, Map<String, Object>>> future : resultFuture) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                log.info("统计失败");
            }
        }
        return result;
    }

    @Override
    public long unFinishCount(Long startTime, Long endTime, List<String> unitIds) {
        return dao.unFinishCount(startTime, endTime, unitIds);
    }

    @Override
    public long timeOutCount(long startTime, long endTime, List<String> unitIds) {
        return dao.timeOutCount(startTime, endTime, unitIds);
    }

    @Override
    public long onTimeCount(long startTime, long endTime, List<String> unitIds) {
        return dao.onTimeCount(startTime, endTime, unitIds);
    }

    public List<HashMap> checkData(List<HashMap> data, int count) {
        data = new ArrayList(data);
        Calendar start = Calendar.getInstance();
        start.add(Calendar.YEAR, -1 * count);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, -1 * count + 1);

        LinkedList<HashMap> list = new LinkedList<>();
        while (start.getTime().getTime() <= end.getTime().getTime()) {
            int year = start.get(Calendar.YEAR);
            int month = start.get(Calendar.MONTH) + 1;
            if (data.size() > 0) {
                HashMap map = data.get(0);
                if (year == (int) map.get("year") && month == (int) map.get("month")) {
                    list.add(map);
                    data.remove(0);
                } else {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("year", year);
                    item.put("month", month);
                    item.put("count", 0);
                    list.add(item);
                }
            } else {
                HashMap<String, Object> item = new HashMap<>();
                item.put("year", year);
                item.put("month", month);
                item.put("count", 0);
                list.add(item);
            }
            start.add(Calendar.MONTH, 1);
        }
        return list;
    }

    /**
     * 计算街区高发案卷类型ids
     *
     * @param unitIds   街区ids
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      前几项高发案卷
     * @return 高发案卷类别ids
     */
    public Map<String, Integer> getMaxCategoryIds(List<String> unitIds, long startTime, long endTime, int size) {
        //查找前6的高发案卷
        Map<String, Integer> categorys = new HashedMap<>();
        dao.countByUnitId(startTime, endTime, unitIds).forEach(map -> {
            categorys.put((String) map.get("_id"), (Integer) map.get("count"));
        });
        List<Integer> collect = categorys.values().stream().sorted(Comparator.comparingInt(a -> a)).collect(Collectors.toList());
        Map<String, Integer> data = new HashMap<>();
        List<Integer> categoryCount = new ArrayList<>();
        while (collect.size() > 0 && categoryCount.size() < size) {
            categoryCount.add(collect.remove(collect.size() - 1));
        }
        while (categoryCount.size() > 0) {
            Integer count = categoryCount.remove(0);
            for (String key : categorys.keySet()) {
                if (categorys.get(key) - count == 0) {
                    data.put(key, count);
                    categorys.remove(key);
                    break;
                }
            }
        }
        return data;
    }
}