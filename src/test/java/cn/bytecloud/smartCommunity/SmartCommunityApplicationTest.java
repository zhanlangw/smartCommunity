package cn.bytecloud.smartCommunity;

import cn.bytecloud.smartCommunity.address.dao.AddressRepository;
import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.dao.NodeRepository;
import cn.bytecloud.smartCommunity.smallCategory.dao.SmallCategoryRepository;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.unit.dao.UnitRepository;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.dao.UserRepository;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.MD5Util;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.dao.WorkDao;
import cn.bytecloud.smartCommunity.work.dao.WorkRepository;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartCommunityApplicationTest {


    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private WorkRepository repository;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private SmallCategoryRepository smallCategoryRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private WorkDao dao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private WorkDao workDao;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void password(){
        List<JSONObject> users = mongoTemplate.findAll(JSONObject.class, "aa_user");
        for (JSONObject user : users) {
//            JSONObject user = JSONObject.parseObject(object.toString());
            String unitId = user.getString("s_unit_id");
            List<String> list = new ArrayList<>();
            list.add(unitId);
            user.put("s_unit_id", list);
            mongoTemplate.save(user,"aa_user");
        }
    }

    @Test
    public void updateDate(){
        List<Work> list = workRepository.findAll();
        List<Work> data = new ArrayList<>();
        Random random = new Random();
        int size = list.size();
        for (int i = 1;i<12;i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i * -1);
            long time = calendar.getTime().getTime();
            for (int j = 0;j<10000;j++) {
                Work work = list.get(random.nextInt(size));
                work.setCreateTime(time);
                work.setId(UUIDUtil.getUUID());
                work.setCreateTime(time);
                data.add(work);
            }
        }
        workRepository.save(data);
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5("123456"));
    }

    @Test
    public void stats() {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_UNIT_ID).is("05ba39d264ba488aa9c2152249a65c72")));

        Criteria criteria1 = new Criteria().andOperator(Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()));
        Criteria criteria2 = new Criteria().andOperator(Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis
                ()));

        list.add(
                Aggregation.group(ModelConstant.WORK_UNIT_ID)
                        .sum(ConditionalOperators.when(new Criteria().orOperator(
                                criteria1, criteria2
                        )).then(2).otherwise(0)).as("timeOutCount")
                        .sum(ConditionalOperators.when(
                                Criteria.where(ModelConstant.WORK_END_FLAG).is(true)
                        ).then(1).otherwise(0)).as("finishCount")
                        .sum(ConditionalOperators.when(
                                Criteria.where(ModelConstant.WORK_END_FLAG).is(false)
                        ).then(1).otherwise(0)).as("unFinishCount")
        );

//        list.add(
//                Aggregation.match(new Criteria().orOperator(
//                        Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()),
//                        Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis())
//                )));


        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, ModelConstant.T_WORK, HashMap.class).getMappedResults();
        System.out.println((long) (dataCount.get(0).get("totalCount")));
    }

    @Test
    public void update() {
        List<AggregationOperation> list = new ArrayList<>();
        long time = StringUtil.getStatsTime(new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + " 00:00:00").getTime();

        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(time)));
        list.add(Aggregation.group(ModelConstant.CREATOR_ID));
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, ModelConstant.T_USER_ADDRESS, HashMap.class).getMappedResults();
        System.out.println((long) (dataCount.get(0).get("totalCount")));
    }

    @Test
    public void device() throws InterruptedException {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Integer b = i;
            ThreadPool.threadPool.execute(() -> {
                Query query = new Query();
//                query.addCriteria(
//                        new Criteria().orOperator(
//                                Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()),
//                                Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis())
//                        )
//                );
                boolean exists = template.exists(query, Work.class);
                long count = template.count(query, Work.class);
                System.out.println(b + "         " + count + "    " + (System.currentTimeMillis() - time));
                System.gc();
            });
        }
//


//        unitService.findByPid(unitService.findByPid(null).get(0).getId()).stream().map(unit -> {
//            List<String> unitIds = unitService.findAllByPid(unit.getId());
//            unitIds.add(unit.getId());
//            Map<String, Integer> categorys = new HashedMap<>();
//            dao.countByUnitId(0, time, unitIds).forEach(map -> {
//                categorys.put((String) map.get("_id"), (Integer) map.get("count"));
//            });
//            List<Integer> collect = categorys.values().stream().sorted(Comparator.comparingInt(a -> a)).collect(Collectors.toList());
//            Map<String, Integer> data = new HashMap<>();
//            List<Integer> categoryCount = new ArrayList<>();
//            while (collect.size() > 0 && categoryCount.size() < 10) {
//                categoryCount.add(collect.remove(collect.size() - 1));
//            }
//            while (categoryCount.size() > 0) {
//                Integer count = categoryCount.remove(0);
//                for (String key : categorys.keySet()) {
//                    if (categorys.get(key) - count == 0) {
//                        data.put(key, count);
//                        break;
//                    }
//                }
//            }
//            System.out.println(System.currentTimeMillis() - time);
//            return data;
//        }).collect(Collectors.toList()).forEach(item->{
//            System.out.println(item.toString());
//        });


//       unitService.findByPid(unitService.findByPid(null).get(0).getId()).stream().map(unit ->
//                CompletableFuture.supplyAsync(() -> {
//            List<String> unitIds = unitService.findAllByPid(unit.getId());
//            unitIds.add(unit.getId());
//            Map<String, Integer> categorys = new HashedMap<>();
//            dao.countByUnitId(0, time, unitIds).forEach(map -> {
//                categorys.put((String) map.get("_id"), (Integer) map.get("count"));
//            });
//            List<Integer> collect = categorys.values().stream().sorted(Comparator.comparingInt(a -> a)).collect(Collectors.toList());
//            Map<String, Integer> data = new HashMap<>();
//            List<Integer> categoryCount = new ArrayList<>();
//            while (collect.size() > 0 && categoryCount.size() < 10) {
//                categoryCount.add(collect.remove(collect.size() - 1));
//            }
//            while (categoryCount.size() > 0) {
//                Integer count = categoryCount.remove(0);
//                for (String key : categorys.keySet()) {
//                    if (categorys.get(key) - count == 0) {
//                        data.put(key, count);
//                        break;
//                    }
//                }
//            }
//            System.out.println(System.currentTimeMillis() - time);
//            return data;
//        }, ThreadPool.threadPool)).collect(Collectors.toSet()).forEach(item->{
//           try {
//               System.out.println(item.get().toString());
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           } catch (ExecutionException e) {
//               e.printStackTrace();
//           }
//       });

        Thread.sleep(200000);
//        Query query = new Query();
//        query.addCriteria(
//                new Criteria().orOperator(
//                        Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()),
//                        Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis())
//                )
//        );
//        long count = template.count(query, Work.class);
//        System.out.println(1+"         "+count + "    " + (System.currentTimeMillis() - time));
//        System.gc();
    }

    @Test
    public void test() throws ByteException, UnsupportedEncodingException {
//        for (Work work : workRepository.findAll()) {
//            long createTime = work.getCreateTime();
//            Calendar instance = Calendar.getInstance();
//            instance.setTime(new Date(createTime));
//            instance.add(Calendar.MONTH, 1);
//            work.setCreateYear(instance.get(Calendar.YEAR));
//            work.setCreateMonth(instance.get(Calendar.MONTH));
//            work.setCreateDay(instance.get(Calendar.DAY_OF_MONTH));
//            instance = Calendar.getInstance();
//            instance.setTime(new Date(work.getFinishTime()));
//            instance.add(Calendar.MONTH, 1);
//            work.setFinishYear(instance.get(Calendar.YEAR));
//            work.setFinishMonth(instance.get(Calendar.MONTH));
//            work.setFinishDay(instance.get(Calendar.DAY_OF_MONTH));
//            workRepository.save(work);
//        }
//        List<SmallCategory> categories = smallCategoryRepository.findAll();
//        List<Work> all = workRepository.findAll();
//        System.out.println(all.size());
//        int count = 1;
//        for (Work work : all) {
//            System.out.println(count++);
//            for (int a = 0; a < 13; a++) {
//                Calendar instance = Calendar.getInstance();
//                instance.setTime(new Date(System.currentTimeMillis()));
//                instance.add(Calendar.MONTH, a * -1);
//                work.setCreateTime(instance.getTime().getTime());
//                work.setCreateYear(instance.get(Calendar.YEAR));
//                work.setCreateMonth(instance.get(Calendar.MONTH) + 1);
//                work.setCreateDay(instance.get(Calendar.DAY_OF_MONTH));
//
//                System.out.println(instance.get(Calendar.YEAR));
//                System.out.println(instance.get(Calendar.MONTH) + 1);
//
//                instance = Calendar.getInstance();
//                instance.setTime(new Date(System.currentTimeMillis()));
//                instance.add(Calendar.MONTH, a * -1);
//                work.setFinishYear(instance.get(Calendar.YEAR));
//                work.setFinishMonth(instance.get(Calendar.MONTH) + 1);
//                work.setFinishDay(instance.get(Calendar.DAY_OF_MONTH));
//
//                for (int i = 0; i < 100; i++) {
//                    work.setId(UUIDUtil.getUUID());
//                    work.setTypeId(categories.get(new Random().nextInt(categories.size() - 1)).getId());
//                    workRepository.save(work);
//                }
//            }
//        }
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where(ModelConstant.WORK_CREATE_MONTH).is(null),
                Criteria.where(ModelConstant.WORK_CREATE_YEAR).is(null),
                Criteria.where(ModelConstant.WORK_CREATE_DAY).is(null)
        ));
        template.remove(query, Work.class);
    }

    @Test
    public void contextLoads() {
        addressRepository.findAll().forEach(address -> {
            Integer num = address.getNum();
            Unit unit = null;
            if (num == 0) {
                unit = unitRepository.findByAbbre("01");
            }
            if (num == 1) {
                unit = unitRepository.findByAbbre("02");
            }
            if (num == 2) {
                unit = unitRepository.findByAbbre("03");
            }
            if (num == 3) {
                unit = unitRepository.findByAbbre("04");
            }
            if (num == 4) {
                unit = unitRepository.findByAbbre("05");
            }
            if (num == 5) {
                unit = unitRepository.findByAbbre("05");
            }
            if (num == 6) {
                unit = unitRepository.findByAbbre("06");
            }
            if (num == 7) {
                unit = unitRepository.findByAbbre("07");
            }
            if (num == 8) {
                unit = unitRepository.findByAbbre("08");
            }
            if (num == 9) {
                unit = unitRepository.findByAbbre("09");
            }
            if (num == 10) {
                unit = unitRepository.findByAbbre("10");
            }
            if (num == 11) {
                unit = unitRepository.findByAbbre("11");
            }
            if (num == 12) {
                unit = unitRepository.findByAbbre("12");
            }
            if (num == 13) {
                unit = unitRepository.findByAbbre("13");
            }
            if (num == 14) {
                unit = unitRepository.findByAbbre("14");
            }
            if (num == 15) {
                unit = unitRepository.findByAbbre("14");
            }
            if (num == 16) {
                unit = unitRepository.findByAbbre("15");
            }
            if (num == 17) {
                unit = unitRepository.findByAbbre("16");
            }
            if (unit == null) {
                throw new RuntimeException();
            }
            address.setUnitId(unit.getId());
            addressRepository.save(address);
        });
    }

    @Test
    public void removeTodo() {
        List<String> unitIds = unitService.findAllByPid("f5d94e9cc6464c7087f30be5ad630eca");
        unitIds.add("f5d94e9cc6464c7087f30be5ad630eca");
        //查找前6的高发案卷
        Map<String, Integer> categorys = new HashedMap<>();
        dao.countByUnitId(0, System.currentTimeMillis(), unitIds).forEach(map -> {
            categorys.put((String) map.get("_id"), (Integer) map.get("count"));
        });
        List<Integer> collect = categorys.values().stream().sorted(Comparator.comparingInt(a -> a)).collect(Collectors.toList());
        Map<String, Integer> data = new HashMap<>();
        List<Integer> categoryCount = new ArrayList<>();
        while (collect.size() > 0 && categoryCount.size() < 20) {
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
        System.out.println();
    }

    @Test
    public void tests() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://106.15.238.82:19524/sms-partner/access/b07898/sendsms";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientid", "b07898");
        jsonObject.put("password", "e9bc0e13a8a16cbb07b175d92a113126");
        jsonObject.put("mobile", "15756378324");
        jsonObject.put("smstype", "0");
        jsonObject.put("content", "【安徽易木】测试短信");
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        System.out.println(respose.toString());
    }

    @Test
    public void dbobject() {
        DBObject dbObject = new BasicDBObject();

        DBObject fieldObject = new BasicDBObject();

        fieldObject.put(ModelConstant.WORK_TITLE, true);

        fieldObject.put(ModelConstant.ID, true);
        Query query = new BasicQuery(dbObject, fieldObject);
        query.addCriteria(Criteria.where(ModelConstant.WORK_TITLE).is("标题"));
        List<Work> works = template.find(query, Work.class);
        System.out.println();

    }

    @Test
    public void pageAble() {
        Pageable pageable = new PageRequest(0, 10,new Sort(Sort.Direction.DESC,"_id"));
        SmallCategory category = new SmallCategory();
        category.setName("a");
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());
        Example<SmallCategory> example = Example.of(category,matcher);
        Page<SmallCategory> all = smallCategoryRepository.findAll(example,pageable);
        System.out.println();
    }
}

