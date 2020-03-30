package cn.bytecloud.smartCommunity.device.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.device.dto.DevicePageDto;
import cn.bytecloud.smartCommunity.device.dto.PageAppListDto;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.*;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Repository
public class DeviceDaoImpl extends BaseDao<Device> implements DeviceDao {
    @Autowired
    private DeviceRepository repository;
    @Autowired
    private MongoTemplate template;

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5("root"));
    }

    @Override
    public Device save(Device entity) {
        if (EmptyUtil.isEmpty(entity.getId())) {
            entity.setId(UUIDUtil.getUUID());
            entity.setCreateTime(System.currentTimeMillis());
            entity.setCreatorId(UserUtil.getUserId());
        } else {
            Device old = repository.findOne(entity.getId());
            entity.setCreateTime(old.getCreateTime());
            entity.setCreatorId(old.getCreatorId());
        }
        entity.setUpdateTime(System.currentTimeMillis());
        repository.save(entity);
        return entity;
    }

    @Override
    public PageModel<HashMap> list(DevicePageDto dto) {
        List<AggregationOperation> list = new ArrayList<>();

        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where(DEVICE_NAME).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(DEVICE_NUM).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(DEVICE_ADDRESS).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(DEVICE_LONGITUDE).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(DEVICE_LATITUDE).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(DEVICE_IP).regex(StringUtil.translat(dto.getName()))
            )));
        }

        list.add(LookupOperation.newLookup()
                .from(T_UNIT)
                .localField(DEVICE_UNIT_ID)
                .foreignField(ID)
                .as("unit")
        );
        list.add(Aggregation.unwind("unit"));

        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(DEVICE_NAME).as("name")
                .and(DEVICE_NUM).as("num")
                .and(DEVICE_ADDRESS).as("address")
                .and(DEVICE_LATITUDE).as("latitude")
                .and(DEVICE_LONGITUDE).as("longitude")
                .and("unit." + UNIT_NAME).as("unitName")
                .and(DEVICE_TYPE).as("type")
                .and(DEVICE_FEATURES).as("features")
                .and(DEVICE_IP).as("ip")
                .and(ID).as("id")
                .and(DEVICE_STATUS).as("status")
                .andExclude(ID)
        );

        return pageList(list, dto, T_DEVICE, null);
    }

    @Override
    public PageModel<Device> rpcList(BasePageDto dto) {
        return pageList(new Query(), dto, null);
    }

    @Override
    public List<Device> mapList() {
        Query query = QueryUtil.createQuery(ModelConstant.DEVICE_LATITUDE, ModelConstant.DEVICE_LONGITUDE, ModelConstant.DEVICE_NAME);
        query.addCriteria(Criteria.where(ModelConstant.DEVICE_LONGITUDE).ne(null));
        query.addCriteria(Criteria.where(ModelConstant.DEVICE_LATITUDE).ne(null));
        return template.find(query, Device.class);
    }

    @Override
    public PageModel appList(PageAppListDto dto, Set<String> deviceIds) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getUnitId())) {
            query.addCriteria(Criteria.where(ModelConstant.DEVICE_UNIT_ID).is(dto.getUnitId()));
        }
        UserType userType = UserUtil.getUser().getUserType();
        if (userType != UserType.ROOT && userType != UserType.ADMIN) {
            query.addCriteria(Criteria.where(ModelConstant.ID).in(deviceIds));
        }
        long count = template.count(query, Device.class);
        if (count == 0) {
            return PageModel.isEmpty();
        }
        query.skip(dto.getStart());
        query.limit(dto.getCount());
        List<Map> data = new ArrayList<>();
        template.find(query, Device.class).forEach(device -> {
            Map map = new HashedMap();
            map.put("id", device.getId());
            map.put("name", device.getName());
            map.put("unitId", device.getUnitId());
            data.add(map);
        });
        return new PageModel<>(count, data);
    }

    @Override
    public long countByOnline(DeviceStatus online) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.DEVICE_STATUS).is(online));
        return template.count(query, Device.class);
    }

    @Override
    public List<Device> findByName(String title) {
        return template.find(Query.query(Criteria.where(ModelConstant.DEVICE_NAME).regex(StringUtil.translat(title))), Device.class);
    }
}
