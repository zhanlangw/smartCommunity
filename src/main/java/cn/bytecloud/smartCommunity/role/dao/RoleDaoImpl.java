package cn.bytecloud.smartCommunity.role.dao;


import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.role.dto.PageRoleDto;
import cn.bytecloud.smartCommunity.role.entity.Role;
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

import java.util.List;
import java.util.Set;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.CREATE_TIME;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.T_SMALL_CATEORY;

@Repository
public class RoleDaoImpl extends BaseDao<Role> implements RoleDao {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private MongoTemplate template;


    @Override
    public List<Role> findByIds(Set<String> roleIds) {
        Query query = new Query();
       // query.addCriteria(Criteria.where("id").in(roleIds));
        query.addCriteria(Criteria.where(ID).in(roleIds));
        List<Role> list = template.find(query, Role.class);
        return list;
    }

    @Override
    public Role save(Role role) {
        if (EmptyUtil.isEmpty(role.getId())) {
            role.setId(UUIDUtil.getUUID());
            role.setCreateTime(System.currentTimeMillis());
            role.setCreatorId(UserUtil.getUserId());
        } else {
            Role old = repository.findOne(role.getId());
            role.setCreatorId(old.getCreatorId());
            role.setCreateTime(old.getCreateTime());
        }
        role.setUpdateTime(System.currentTimeMillis());
        repository.save(role);
        return role;
    }

    @Override
    public Object list(PageRoleDto dto) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(Criteria.where(ROLE_NAME).regex(StringUtil.translat(dto.getName()))));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        list.add(Aggregation.unwind("user"));

        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(StringUtil.translat(dto.getCreator()))));
        }

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(ROLE_NAME).as("name")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_ROLE, CREATE_TIME);
    }
}
