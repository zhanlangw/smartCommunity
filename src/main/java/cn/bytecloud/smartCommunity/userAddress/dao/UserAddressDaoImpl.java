package cn.bytecloud.smartCommunity.userAddress.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.userAddress.dto.UserAddressPageDto;
import cn.bytecloud.smartCommunity.userAddress.entity.UserAddress;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public class UserAddressDaoImpl extends BaseDao<UserAddress> implements UserAddressDao {
    @Autowired
    private UserAddressRepository repository;
    @Autowired
    private MongoTemplate template;

    @Autowired
    private SystemConstant systemConstant;

    @Override
    public List<UserAddress> list(UserAddressPageDto dto) {
        Query query = getQuery(dto.getStartTime(), dto.getEndTime(), ModelConstant.CREATE_TIME);
        query.addCriteria(Criteria.where(ModelConstant.CREATOR_ID).is(dto.getUserId()));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.CREATE_TIME));
        return template.find(query, UserAddress.class);
    }

    @Override
    public List<HashMap> supervision(Set<String> userIds) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATOR_ID).in(userIds)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(System.currentTimeMillis()-systemConstant.addressTimeOut)));
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME)));
        list.add(Aggregation.
                group(ModelConstant.CREATOR_ID).
                first(ModelConstant.USER_ADDRESS_LONGITUDE).as("longitude").
                first(ModelConstant.USER_ADDRESS_LATITUDE).as("latitude")
        );
        return aggregat(list, ModelConstant.T_USER_ADDRESS);
    }

    @Override
    public UserAddress findOneByUserId() {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.CREATOR_ID).is(UserUtil.getUserId()));
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        query.limit(1);
        return template.findOne(query, UserAddress.class);
    }

    @Override
    public long onlineCount(long time) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(time)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.USER_ADDRESS_ONLINE_FLAG).is(true)));
        list.add(Aggregation.group(ModelConstant.CREATOR_ID));
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, ModelConstant.T_USER_ADDRESS, HashMap.class).getMappedResults();
        return dataCount.size();
    }

    @Override
    public long onlineCountByIds(long time, Set<String> userIds) {
        List<AggregationOperation> list = new ArrayList<>();
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATE_TIME).gte(time)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.CREATOR_ID).in(userIds)));
        list.add(Aggregation.match(Criteria.where(ModelConstant.USER_ADDRESS_ONLINE_FLAG).is(true)));
        list.add(Aggregation.group(ModelConstant.CREATOR_ID));
        AggregationOptions aggregationOptions = new AggregationOptions.Builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(list).withOptions(aggregationOptions);
        List<HashMap> dataCount = template.aggregate(aggregation, ModelConstant.T_USER_ADDRESS, HashMap.class).getMappedResults();
        return dataCount.size();
    }
}
