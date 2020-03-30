package cn.bytecloud.smartCommunity.smallCategory.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Repository
public class SmallCategoryDaoImpl extends BaseDao<SmallCategory> implements SmallCategoryDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private SmallCategoryRepository repository;

    /**
     * 添加
     *
     * @param category
     * @return
     */
    @Override
    public SmallCategory save(SmallCategory category) {
        if (EmptyUtil.isEmpty(category.getId())) {
            category.setId(UUIDUtil.getUUID());
            category.setCreateTime(System.currentTimeMillis());
            category.setCreatorId(UserUtil.getUserId());
        } else {
            SmallCategory old = repository.findOne(category.getId());
            category.setCreateTime(old.getCreateTime());
            category.setCreatorId(old.getCreatorId());
        }
        category.setUpdateTime(System.currentTimeMillis());
        repository.save(category);
        return category;
    }

    @Override
    public PageModel<HashMap> list(CategoryPageDto dto) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getId())) {
            list.add(Aggregation.match(Criteria.where(SMALL_CATEORY_BIG_ID).is(dto.getId())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        list.add(Aggregation.unwind("user"));

        list.add(LookupOperation.newLookup()
                .from(T_UNIT)
                .localField(SMALL_CATEORY_UNIT_ID)
                .foreignField(ID)
                .as("unit")
        );
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(SMALL_CATEORY_NAME).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(SMALL_CATEORY_ABBRE).regex(StringUtil.translat(dto.getName())),
                    Criteria.where("unit." + UNIT_NAME).regex(StringUtil.translat(dto.getName()))
            )));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(SMALL_CATEORY_NAME).as("name")
                .and(SMALL_CATEORY_ABBRE).as("abbre")
                .and("unit." + UNIT_NAME).as("unitName")
                .and(SMALL_CATEORY_TYPE).as("workType")
                .and(DESC).as("desc")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_SMALL_CATEORY, CREATE_TIME);
    }

    @Override
    public List<SmallCategory> findByName(String categoryName) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.SMALL_CATEORY_NAME).regex(StringUtil.translat(categoryName)));
        return template.find(query, SmallCategory.class);
    }
}
