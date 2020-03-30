package cn.bytecloud.smartCommunity.work.dao;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.blacklist.service.BlacklistService;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dao.SmallCategoryRepository;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.stats.dto.StatsDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.*;
import cn.bytecloud.smartCommunity.work.dto.WrokPageDto;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
import static cn.bytecloud.smartCommunity.constant.SystemConstant.INIT_SYSTEM_ID;

@Repository
public class WorkDaoImpl extends BaseDao<Work> implements WorkDao {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private WorkRepository repository;

    @Autowired
    private SmallCategoryService smallCategoryService;
    @Autowired
    private BasisService basisService;
    @Autowired
    private UserService userService;
    @Autowired
    private BlacklistService blacklistService;

    public static void main(String[] args) {
        for (int a = 0; a < 13; a++) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date(System.currentTimeMillis()));
            instance.add(Calendar.MONTH, a * -1);
            System.out.println(instance.get(Calendar.YEAR));
            System.out.println(instance.get(Calendar.MONTH) + 1);
        }

    }

    /**
     * 保存
     *
     * @param work
     * @return
     */
    @Override
    public Work save(Work work) {
        if (EmptyUtil.isEmpty(work.getId())) {
            if (work.isAcceptFlag()) {
                work.setCreatorId(UserUtil.getUserId());
            } else {
                work.setCreatorId(INIT_SYSTEM_ID);
            }

            work.setId(UUIDUtil.getUUID());
            work.setCreateTime(System.currentTimeMillis());
        } else {
            Work old = repository.findOne(work.getId());
            if (old.isAcceptFlag()) {
                work.setCreateTime(old.getCreateTime());
                work.setCreateDay(old.getCreateDay());
                work.setCreateMonth(old.getCreateMonth());
                work.setCreateYear(old.getCreateYear());
            } else {
                work.setCreateTime(System.currentTimeMillis());
            }
            work.setCreatorId(old.getCreatorId());
        }
        work.setUpdateTime(System.currentTimeMillis());
        repository.save(work);
        return work;
    }

    @Override
    public Optional<Work> findLast() {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        query.limit(1);
        Work work = template.findOne(query, Work.class);
        return work == null ? Optional.empty() : Optional.of(work);
    }

    @Override
    public long countByTypeId(String typeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_TYPE_ID).is(typeId));
        return template.count(query, Work.class);
    }

    @Override
    public Object todoList(WrokPageDto dto, Basis basis) {
        List<AggregationOperation> list = addMatch(dto, ModelConstant.CREATE_TIME);
        User user = UserUtil.getUser();
        if (user.getUserType() != UserType.ROOT && user.getUserType() != UserType.ADMIN) {
            list.add(Aggregation.match(Criteria.where(TODO_HANDLER_IDS).elemMatch(new Criteria().in(user.getId()))));
        }
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_TITLE).regex(StringUtil.translat(dto.getTitle()))));
        }
        if (EmptyUtil.isNotEmpty(dto.getNum())) {
            list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_NUM).regex(StringUtil.translat(dto.getNum()))));
        }

        list.add(Aggregation.match(Criteria.where(TODO_TYPE).is(dto.getTodoType().name())));
        if (dto.getSource() != null) {
            list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_WORK_SOURCE).is(dto.getSource().name())));
        }
        if (dto.getWorkType() != null) {
            list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_WORK_TYPE).is(dto.getWorkType().name())));
        }
        if (EmptyUtil.isNotEmpty(dto.getFilterField())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(ModelConstant.TODO_TITLE).regex(StringUtil.translat(dto.getFilterField())),
                    Criteria.where(ModelConstant.TODO_NUM).regex(StringUtil.translat(dto.getFilterField()))
            )));
        }

        list.add(LookupOperation.newLookup()
                .from(T_SMALL_CATEORY)
                .localField(TODO_WORK_TYPE_ID)
                .foreignField(ID)
                .as("category")
        );
        list.add(Aggregation.unwind("category"));

        if (EmptyUtil.isNotEmpty(dto.getSearchField())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(ModelConstant.TODO_TITLE).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.TODO_NUM).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where("category." + SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.TODO_ADDRESS).regex(StringUtil.translat(dto.getSearchField()))

            )));
        }


        if (EmptyUtil.isNotEmpty(dto.getCategoryName())) {
            list.add(Aggregation.match(Criteria.where("category." + SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getCategoryName()))));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        list.add(Aggregation.unwind("user"));

        if (dto.getStatus() != null) {
            if (dto.getStatus() == WorkStatus.ORDINARY) {
                list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_END_TIME).gte(System.currentTimeMillis() + basis.getTimeOut()
                        * 3600 * 1000)));
            } else if (dto.getStatus() == WorkStatus.TIME_OUT) {
                list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_END_TIME).lte(System.currentTimeMillis())));
            } else {
                list.add(Aggregation.match(Criteria.where(ModelConstant.TODO_END_TIME).gte(System.currentTimeMillis()).lte(System.currentTimeMillis() + basis.getTimeOut()
                        * 3600 * 1000)));
            }
        }

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(TODO_NUM).as("num")
                .and(TODO_TITLE).as("title")
                .and(TODO_END_TIME).as("endTime")
                .and(TODO_TIME).as("time")
                .and(TODO_TIME_TYPE).as("timeType")
                .and("category." + SMALL_CATEORY_NAME).as("categoryName")
                .and(TODO_WORK_TYPE).as("workType")
                .and(TODO_WORK_SOURCE).as("source")
                .and(TODO_ADDRESS).as("address")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );
        PageModel<HashMap> pageModel = pageList(list, dto, T_TODO, CREATE_TIME);
        for (HashMap map : pageModel.getValue()) {
            Long entTime = (Long) map.remove("endTime");
            map.put("entTime", StringUtil.getTime(new Date(entTime)));
            map.put("status", WorkStatus.getStatus(entTime, basis, System.currentTimeMillis()));
        }
        return pageModel;
    }

    @Override
    public Object acceptList(WrokPageDto dto) {
        List<AggregationOperation> list = addMatch(dto, ModelConstant.CREATE_TIME);
        User user = UserUtil.getUser();
        if (user.getUserType() != UserType.ROOT && user.getUserType() != UserType.ADMIN) {
            return PageModel.isEmpty();
        }

        list.add(Aggregation.match(Criteria.where(WORK_ACCEPT_FLAG).is(false)));
        if (dto.getWorkType() != null) {
            list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_WORK_TYPE).is(dto.getWorkType().name())));
        }
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(WORK_TITLE).regex(StringUtil.translat(dto.getTitle()))));
        }

        list.add(LookupOperation.newLookup()
                .from(T_SMALL_CATEORY)
                .localField(WORK_TYPE_ID)
                .foreignField(ID)
                .as("category")
        );
        list.add(Aggregation.unwind("category"));

        if (EmptyUtil.isNotEmpty(dto.getSearchField())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(ModelConstant.WORK_TITLE).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_NUM).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where("category." + SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_ADDRESS).regex(StringUtil.translat(dto.getSearchField()))

            )));
        }


        if (EmptyUtil.isNotEmpty(dto.getCategoryName())) {
            list.add(Aggregation.match(Criteria.where("category." + SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getCategoryName()))));
        }

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, UPDATE_TIME)));

        list.add(Aggregation.project()
                .and(WORK_TITLE).as("title")
                .and("category." + SMALL_CATEORY_NAME).as("categoryName")
                .and(WORK_WORK_TYPE).as("workType")
                .and(WORK_ADDRESS).as("address")
                .and(WORK_HANDLE_BEFORE_IMAGE_PATHS).as("image")
                .and(WORK_BLACKLIST_ID).as("blacklistId")
                .and(CREATE_TIME).as("createTime")
                .and(UPDATE_TIME).as("updateTime")
                .and(ID).as("id")
                .andExclude(ID)
        );
        PageModel<HashMap> pageModel = pageList(list, dto, T_WORK, CREATE_TIME);
        for (HashMap hashMap : pageModel.getValue()) {
            Blacklist blacklist = blacklistService.findById((String) hashMap.remove("blacklistId"));
            hashMap.put("blacklistName", blacklist == null ? "" : blacklist.getName());
            hashMap.put("updateTime", StringUtil.getTime(new Date((long) hashMap.remove("updateTime"))));
            List<String> image = (List<String>) hashMap.remove("image");
            Collections.reverse(image);
            hashMap.put("image", image);
        }
        return pageModel;

    }

    @Override
    public Object finishList(WrokPageDto dto) {
        Query query = new Query();

        User user = UserUtil.getUser();
        if (user.getUserType() != UserType.ROOT && user.getUserType() != UserType.ADMIN) {
            query.addCriteria(Criteria.where(WORK_READER_IDS).elemMatch(new Criteria().in(user.getId())));
        }
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_TITLE).regex(StringUtil.translat(dto.getTitle())));
        }
        if (EmptyUtil.isNotEmpty(dto.getNum())) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_NUM).regex(StringUtil.translat(dto.getNum())));
        }
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(true));
        List<SmallCategory> categories;
        if (EmptyUtil.isNotEmpty(dto.getCategoryName())) {
            categories = smallCategoryService.findByName(dto.getCategoryName());
            query.addCriteria(Criteria.where(ModelConstant.WORK_TYPE_ID).in(categories.stream().map(SmallCategory::getId).collect(Collectors.toList())));
        } else {
            categories = smallCategoryService.findAll();
        }
        if (dto.getWorkType() != null) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_WORK_TYPE).is(dto.getWorkType()));
        }
        if (dto.getSource() != null) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_SOURCE).is(dto.getSource()));
        }

        if (EmptyUtil.isNotEmpty(dto.getSearchField())) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(ModelConstant.WORK_TITLE).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_NUM).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_ADDRESS).regex(StringUtil.translat(dto.getSearchField()))

            ));
        }
        query.with(new Sort(Sort.Direction.DESC, WORK_FINISH_TIME));
        Basis basis = basisService.findFirst();
        PageModel pageModel = pageList(query, dto, ModelConstant.WORK_FINISH_TIME);
        List<Map<String, java.io.Serializable>> data = new ArrayList<>();
        for (Object object : pageModel.getValue()) {
            Work work = (Work) object;
            Map<String, java.io.Serializable> map = new HashedMap<>();
            map.put("id", work.getId());
            map.put("title", work.getTitle());
            map.put("num", work.getNum());
            map.put("categoryName", categories.stream().filter(category -> work.getTypeId().equals(category.getId())).findFirst().get().getName());
            map.put("workStatus", WorkStatus.getStatus(work.getEndTime(), basis, work.getFinishTime()));
            map.put("workType", work.getWorkType());
            map.put("source", work.getSource());
            map.put("address", work.getAddress());
            map.put("creator", userService.findById(work.getCreatorId()).getName());
            map.put("createTime", StringUtil.getTime(new Date(work.getCreateTime())));
            map.put("finishTime", StringUtil.getTime(new Date(work.getFinishTime())));
            data.add(map);
        }
        pageModel.setValue(data);
        return pageModel;
    }

    @Override
    public List<Work> alarmList(Map<String, String> map) {
        Query query = QueryUtil.createQuery(ModelConstant.WORK_SOURCE, ModelConstant.WORK_TYPE_ID, ModelConstant.WORK_HANDLE_BEFORE_IMAGE_PATHS, ModelConstant.WORK_LATITUDE,
                ModelConstant.WORK_LONGITUDE, ModelConstant.CREATE_TIME);
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(false));
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(ModelConstant.WORK_LATITUDE).ne(null));
        query.addCriteria(Criteria.where(ModelConstant.WORK_LONGITUDE).ne(null));
        query.addCriteria(Criteria.where(ModelConstant.WORK_TYPE_ID).in(map.keySet()));
        return template.find(query, Work.class);
    }

    @Override
    public Object historyList(WrokPageDto dto) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getSearchField())) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(ModelConstant.WORK_TITLE).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_NUM).regex(StringUtil.translat(dto.getSearchField())),
                    Criteria.where(ModelConstant.WORK_ADDRESS).regex(StringUtil.translat(dto.getSearchField()))

            ));
        }
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        if (EmptyUtil.isNotEmpty(dto.getNum())) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_NUM).regex(StringUtil.translat(dto.getNum())));
        }
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_TITLE).regex(StringUtil.translat(dto.getTitle())));
        }
        if (null != dto.getSource()) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_SOURCE).is(dto.getSource()));
        }
        if (null != dto.getWorkType()) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_WORK_TYPE).is(dto.getWorkType()));
        }

        Query categoryQuery = QueryUtil.createQuery(ModelConstant.SMALL_CATEORY_NAME);
        Map<String, String> category = new HashedMap<>();
        if (EmptyUtil.isNotEmpty(dto.getCategoryName())) {
            categoryQuery.addCriteria(Criteria.where(ModelConstant.SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getCategoryName())));
        }
        template.find(categoryQuery, SmallCategory.class).forEach(item -> category.put(item.getId(), item.getName()));

        query.addCriteria(Criteria.where(ModelConstant.WORK_TYPE_ID).in(category.keySet()));

        User user = UserUtil.getUser();
//        if (user.getUserType() != UserType.ADMIN && user.getUserType() != UserType.ROOT) {
        if (user.getUserType() != UserType.ROOT) {
            query.addCriteria(Criteria.where(ModelConstant.WORK_READER_IDS).elemMatch(new Criteria().in(user.getId())));
        }
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        PageModel<Work> pageModel = pageList(query, dto, ModelConstant.CREATE_TIME);
        Basis basis = basisService.findFirst();
        List<Map<String, Object>> data = new ArrayList<>();
        pageModel.getValue().forEach(work -> {
            Map<String, Object> map = new HashedMap<>();
            map.put("id", work.getId());
            map.put("title", work.getTitle());
            map.put("num", work.getNum());
            map.put("source", work.getSource());
            map.put("status", WorkStatus.getStatus(work.getEndTime(), basis, work.isEndFlag() ? work.getFinishTime() : System.currentTimeMillis()));
            map.put("workType", work.getWorkType());
            map.put("address", work.getAddress());
            map.put("categoryName", category.getOrDefault(work.getTypeId(), ""));
            map.put("createTime", StringUtil.getTime(new Date(work.getCreateTime())));
            map.put("creator", work.getSource() == WorkSource.SYSTEM ? WorkSource.SYSTEM.getEnumValue() : userService.findById(work.getCreatorId()).getName());
            data.add(map);
        });
        return new PageModel<>(pageModel.getTotalCount(), data);
    }

    @Override
    public List<Work> findByIds(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).in(ids));
        return template.find(query, Work.class);
    }

    @Override
    public long totalCount(StatsDto dto) throws ByteException {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        return template.count(query, Work.class);
    }

    @Override
    public long finishCount(StatsDto dto) throws ByteException {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(true));
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        return template.count(query, Work.class);
    }

    @Override
    public long unFinishCount(StatsDto dto) throws ByteException {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(false));
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        return template.count(query, Work.class);
    }

    @Override
    public long timeOutCount(StatsDto dto) throws ByteException {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));

        Criteria criteria1 = new Criteria().andOperator(Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()));
        Criteria criteria2 = new Criteria().andOperator( Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis
                ()));
        query.addCriteria(new Criteria().orOperator(
                criteria1,criteria2
        ));
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        return template.count(query, Work.class);
    }

    @Override
    public long alarnCount(StatsDto dto, List<String> categoryIds) throws ByteException {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(ModelConstant.WORK_TYPE_ID).in(categoryIds));
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        return template.count(query, Work.class);
    }

    @Override
    public Object workStats(StatsDto dto, List<Unit> units) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime())));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_UNIT_ID).in(units.stream().map(Unit::getId).collect(Collectors.toList()))));


        Criteria criteria1 = new Criteria().andOperator(Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()));
        Criteria criteria2 = new Criteria().andOperator( Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis
                ()));
        list.add(
                Aggregation.group(ModelConstant.WORK_UNIT_ID)
                        .sum(ConditionalOperators.when(
                                new Criteria().orOperator(
                                        criteria1,criteria2
                                )).then(1).otherwise(0)).as("timeOutCount")
                        .sum(ConditionalOperators.when(
                                Criteria.where(ModelConstant.WORK_END_FLAG).is(true)
                        ).then(1).otherwise(0)).as("finishCount")
                        .sum(ConditionalOperators.when(
                                Criteria.where(ModelConstant.WORK_END_FLAG).is(false)
                        ).then(1).otherwise(0)).as("unFinishCount")
        );
        list.add(LookupOperation.newLookup()
                .from(ModelConstant.T_UNIT)
                .localField(ModelConstant.ID)
                .foreignField(ModelConstant.ID)
                .as("unit")
        );
        list.add(Aggregation.unwind("unit"));

        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, "unit." + ModelConstant.UNIT_NUM)));

        list.add(Aggregation.project()
                .and("timeOutCount").as("timeOutCount")
                .and("finishCount").as("finishCount")
                .and("unFinishCount").as("unFinishCount")
                .and("unit." + ModelConstant.UNIT_NAME).as("unitName")
                .andExclude(ModelConstant.ID)
        );
        return aggregat(list, ModelConstant.T_WORK);
    }

    @Override
    public long count(long startTime, long endTime, List<String> unitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        if (EmptyUtil.isNotEmpty(unitId)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitId));
        }
        return template.count(query, Work.class);
    }

    @Override
    public long countByStatus(long startTime, long endTime, List<String> unitId, WorkStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        query.addCriteria(Criteria.where(WORK_END_FLAG).is(true));
        query.addCriteria(Criteria.where(WORK_STATUS).is(status));
        if (EmptyUtil.isNotEmpty(unitId)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitId));
        }
        return template.count(query, Work.class);
    }

    @Override
    public long finishCount(long startTime, long endTime, List<String> unitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        if (EmptyUtil.isNotEmpty(unitId)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitId));
        }
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(true));
        return template.count(query, Work.class);
    }

    @Override
    public Object trend(long startTime, long endTime) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime)));
        list.add(Aggregation.group(WORK_TYPE_ID).push(WORK_CREATE_MONTH).as(WORK_CREATE_MONTH).push(WORK_CREATE_YEAR).as(WORK_CREATE_YEAR));
        List<HashMap> aggregat = aggregat(list, T_WORK);
        return null;
    }

    @Override
    public List<HashMap> countByUnitId(long startTime, long endTime, List<String> unitId) {
        List<AggregationOperation> list = new ArrayList<>();
        if (EmptyUtil.isNotEmpty(unitId)) {
            list.add(Aggregation.match(Criteria.where(WORK_UNIT_ID).in(unitId)));
        }
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true)));
        list.add(Aggregation.group(WORK_TYPE_ID).count().as("count"));
        return aggregat(list, T_WORK);
    }

    @Override
    public Map<String, List<HashMap>> trendByTypeId(String typeId, long startTime, long endTime, List<String> unitIds) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(startTime).lte(endTime)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_TYPE_ID).is(typeId)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_ACCEPT_FLAG).is(true)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_CREATE_YEAR).ne(null)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_CREATE_MONTH).ne(null)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.WORK_UNIT_ID).in(unitIds)));
        list.add(Aggregation.group(WORK_CREATE_YEAR, WORK_CREATE_MONTH).count().as("count").first(CREATE_TIME).as(CREATE_TIME));
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, CREATE_TIME)));
        list.add(Aggregation.project()
                .and(WORK_CREATE_YEAR).as("year")
                .and(WORK_CREATE_MONTH).as("month")
                .and("count").as("count")
                .andExclude("_id")
        );
        List<HashMap> aggregat = aggregat(list, T_WORK);
        Map<String, List<HashMap>> data = new HashedMap<>();
        data.put(SpringUtils.getBean(SmallCategoryRepository.class).findOne(typeId).getName(), aggregat);
        return data;
    }

    @Override
    public List<Work> map(StatsDto dto) {
        Query query = QueryUtil.createQuery(ID, WORK_LATITUDE, WORK_LONGITUDE);
        query.addCriteria(Criteria.where(CREATE_TIME).gte(dto.getStartTime()).lte(dto.getEndTime()));
        query.addCriteria(Criteria.where(WORK_SOURCE).ne(WorkSource.WEB));
        query.addCriteria(Criteria.where(WORK_LATITUDE).ne(null));
        query.addCriteria(Criteria.where(WORK_LONGITUDE).ne(null));
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        return template.find(query, Work.class);
    }

    @Override
    public void histogram(Unit unit, Long startTime, Long endTime) {

    }

    @Override
    public long unFinishCount(Long startTime, Long endTime, List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        if (EmptyUtil.isNotEmpty(unitIds)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitIds));
        }
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(false));
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        return template.count(query, Work.class);
    }

    @Override
    public Long timeOutCount(Long startTime, Long endTime, List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        query.addCriteria(
                new Criteria().orOperator(
                        Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).is(WorkStatus.TIME_OUT.name()),
                        Criteria.where(ModelConstant.WORK_END_FLAG).is(false).and(ModelConstant.WORK_END_TIME).lte(System.currentTimeMillis())
                )
        );
        if (EmptyUtil.isNotEmpty(unitIds)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitIds));
        }
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        return template.count(query, Work.class);
    }

    @Override
    public long onTimeCount(long startTime, long endTime, List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime));
        query.addCriteria(Criteria.where(ModelConstant.WORK_END_FLAG).is(true).and(ModelConstant.WORK_STATUS).ne(WorkStatus.TIME_OUT.name()));
        if (EmptyUtil.isNotEmpty(unitIds)) {
            query.addCriteria(Criteria.where(WORK_UNIT_ID).in(unitIds));
        }
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        return template.count(query, Work.class);
    }

    @Override
    public Work findAiInspection(String blacklistId, String deviceId, long inspectionTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CREATE_TIME).gte(System.currentTimeMillis()-inspectionTime));
        query.addCriteria(Criteria.where(WORK_DEVICE_ID).is(deviceId));
        query.addCriteria(Criteria.where(WORK_ACCEPT_FLAG).is(true));
        query.addCriteria(Criteria.where(WORK_END_FLAG).is(true));
        return template.findOne(query, Work.class);
    }
}
