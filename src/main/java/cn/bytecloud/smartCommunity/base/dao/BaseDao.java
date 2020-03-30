package cn.bytecloud.smartCommunity.base.dao;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.work.dto.WrokPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.CREATE_TIME;

@Repository
public class BaseDao<T> {
    @Autowired
    private MongoTemplate template;

    public Class<T> getEntityClass() {
        ParameterizedType ptClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clazz = (Class) ptClass.getActualTypeArguments()[0];
        return clazz;
    }

    public PageModel<T> pageList(Query query, BasePageDto dto, String field) {
        Integer start = dto.getStart();
        Integer count = dto.getCount();
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        if (EmptyUtil.isNotEmpty(field)) {
            if (startTime == null) {
                if (endTime != null) {
                    query.addCriteria(Criteria.where(field).lte(endTime));
                }
            } else {
                if (endTime != null) {
                    query.addCriteria(Criteria.where(field).gte(startTime).lte(endTime));
                } else {
                    query.addCriteria(Criteria.where(field).gte(startTime));
                }
            }
        }
        PageModel<T> pageModel = new PageModel<>();
        long totalCount = template.count(query, getEntityClass());
        pageModel.setTotalCount(totalCount);
        if (totalCount == 0) {
            pageModel.setValue(new ArrayList<>());
            return pageModel;
        }
        if (start != null) {
            query.skip(start);
        }
        if (count != null) {
            query.limit(count);
        }
        List<T> list = template.find(query, getEntityClass());
        pageModel.setValue(list);
        return pageModel;
    }

    public PageModel<HashMap> pageList(List<AggregationOperation> list, BasePageDto dto, String collectionName,
                                       String field) {
        Integer start = dto.getStart();
        Integer count = dto.getCount();

        AggregationOperation totalCount = Aggregation.count().as("totalCount");
        list.add(totalCount);

        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, collectionName, HashMap.class).getMappedResults();
        if (dataCount.size() == 0) {
            return PageModel.isEmpty();
        }

        list.remove(totalCount);

        list.add(Aggregation.skip(start));
        list.add(Aggregation.limit(count));
        aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);

        List<HashMap> data = template.aggregate(aggregation, collectionName, HashMap.class).getMappedResults();

        if (EmptyUtil.isNotEmpty(field)) {
            String timeField = CREATE_TIME.equals(field) ? "createTime" : "updateTime";

            data.forEach(item -> item.put(timeField, StringUtil.getTime(new Date((long) item.remove(timeField)))));
        }
        return new PageModel<HashMap>((int) (dataCount.get(0).get("totalCount")), data);
    }

    public List<HashMap> aggregat(List<AggregationOperation> list, String collectionName) {
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        return template.aggregate(aggregation, collectionName, HashMap.class).getMappedResults();
    }

    public List<AggregationOperation> addMatch(BasePageDto dto, String field) {

        List<AggregationOperation> list = new ArrayList<>();

        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();

        if (dto instanceof WrokPageDto) {
            WrokPageDto wrokPageDto = (WrokPageDto) dto;
            if (wrokPageDto.isPopupFlag()) {
                endTime = System.currentTimeMillis();
                startTime = endTime - 10 * 1000;
            }
        }

        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(field).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(field).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(field).gte(startTime)));
            }
        }
        return list;
    }

    public Query getQuery(Long startTime, Long endTime, String field) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(field)) {
            if (startTime == null) {
                if (endTime != null) {
                    query.addCriteria(Criteria.where(field).lte(endTime));
                }
            } else {
                if (endTime != null) {
                    query.addCriteria(Criteria.where(field).gte(startTime).lte(endTime));
                } else {
                    query.addCriteria(Criteria.where(field).gte(startTime));
                }
            }
        }
        return query;
    }
}
