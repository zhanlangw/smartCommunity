package cn.bytecloud.smartCommunity.station.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.entity.Station;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StationDaoImpl extends BaseDao<Station> implements StationDao {

    @Autowired
    private StationRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public Station save(Station station) {
        if (EmptyUtil.isEmpty(station.getId())) {
            station.setId(UUIDUtil.getUUID());
            station.setCreateTime(System.currentTimeMillis());
            station.setCreatorId(UserUtil.getUserId());
        } else {
            Station old = repository.findOne(station.getId());
            station.setCreateTime(old.getCreateTime());
            station.setCreatorId(old.getCreatorId());
        }
        station.setUpdateTime(System.currentTimeMillis());
        repository.save(station);
        return station;
    }

    @Override
    public PageModel<Station> list(StationPageDto dto) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(ModelConstant.STATION_NAME).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(ModelConstant.STATION_ADDRESS).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(ModelConstant.STATION_LATITUDE).regex(StringUtil.translat(dto.getName())),
                    Criteria.where(ModelConstant.STATION_LONGITUDE).regex(StringUtil.translat(dto.getName()))
            ));
        }
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        return pageList(query, dto, ModelConstant.CREATE_TIME);

    }
}
