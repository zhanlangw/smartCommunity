package cn.bytecloud.smartCommunity.blacklist.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistPageDto;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.CREATE_TIME;

@Repository
public class BlacklistDaoImpl extends BaseDao<Blacklist> implements BlacklistDao {
    @Autowired
    private BlacklistRepository repository;

    @Qualifier
    private MongoTemplate template;

    @Override
    public Blacklist save(Blacklist entity, boolean flag) {
        if (flag) {
            entity.setCreateTime(System.currentTimeMillis());
            entity.setCreatorId(UserUtil.getUserId());
        } else {
            Blacklist old = repository.findOne(entity.getId());
            entity.setCreateTime(old.getCreateTime());
            entity.setCreatorId(old.getCreatorId());
        }
        entity.setUpdateTime(System.currentTimeMillis());
        repository.save(entity);
        return entity;
    }

    @Override
    public Object list(BlacklistPageDto dto) {
        List<AggregationOperation> list = new ArrayList<>();



        list.add(LookupOperation.newLookup()
                .from(T_SMALL_CATEORY)
                .localField(BLACKLIST_TYPE_ID)
                .foreignField(ID)
                .as("type")
        );
        list.add(Aggregation.unwind("type"));

        list.add(LookupOperation.newLookup()
                .from(T_BIG_CATEGORY)
                .localField("type."+SMALL_CATEORY_BIG_ID)
                .foreignField(ID)
                .as("big")
        );
        list.add(Aggregation.unwind("big"));

        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(BLACKLIST_NAME).regex(StringUtil.translat(dto.getName())),
                    Criteria.where("type." + SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getName()))
            )));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(BLACKLIST_NAME).as("name")
                .and("type." + SMALL_CATEORY_NAME).as("type")
                .and("big." + BIG_CATEGORY_NAME).as("bigType")
                .and(BLACKLIST_LEFT_IMAGE_PATH).as("leftImagePath")
                .and(BLACKLIST_RIGHT_IMAGE_PATH).as("rightImagePath")
                .and(BLACKLIST_IMAGE_PATH).as("imagePath")
                .and(DESC).as("desc")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_BLACKLIST, CREATE_TIME);
    }
}
