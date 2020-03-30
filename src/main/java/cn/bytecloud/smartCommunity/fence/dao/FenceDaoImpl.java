package cn.bytecloud.smartCommunity.fence.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.fence.dto.FenceItemDto;
import cn.bytecloud.smartCommunity.fence.dto.PageFenceDto;
import cn.bytecloud.smartCommunity.fence.entity.Fence;
import cn.bytecloud.smartCommunity.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FenceDaoImpl extends BaseDao<Fence> implements FenceDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FenceRepository repository;


    @Override
    public Fence save(Fence fence) {
        if (EmptyUtil.isEmpty(fence.getId())) {
            fence.setId(UUIDUtil.getUUID());
            fence.setCreateTime(System.currentTimeMillis());
            fence.setCreatorId(UserUtil.getUserId());
        } else {
            Fence old = repository.findOne(fence.getId());
            fence.setCreatorId(old.getId());
            fence.setCreateTime(old.getCreateTime());
        }
        fence.setUpdateTime(System.currentTimeMillis());
        mongoTemplate.save(fence);
        return fence;
    }

    @Override
    public void del(String ids) {
        mongoTemplate.remove(Query.query(Criteria.where("id").in(Arrays.stream(ids.split(",")).collect(Collectors.toSet()))), Fence.class);
    }

    @Override
    public Object list(PageFenceDto dto,Set<String> deviceIds) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            query.addCriteria(
                    new Criteria().orOperator(
                            Criteria.where(ModelConstant.FENCE_NAME).regex(StringUtil.translat(dto.getTitle()))
                            , Criteria.where(ModelConstant.FENCE_DEVICE_ID).in(deviceIds)
                    )
            );
        }
        Map map = new HashMap();
        long totalCount = mongoTemplate.count(query, Fence.class);
        map.put("totalCount", totalCount);
        if (totalCount == 0) {
            map.put("value", new ArrayList<>());
            return map;

        }
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        List<Fence> list = mongoTemplate.find(query, Fence.class);
        map.put("value", EntityUtil.entityListToDtoList(list, FenceItemDto.class));
        return map;
    }
}
