package cn.bytecloud.smartCommunity.unit.dao;


import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Service
public class UnitDaoImpl implements UnitDao {

    @Autowired
    private MongoTemplate template;


    @Autowired
    private UnitRepository repository;


    /**
     * 添加组织机构
     */
    @Override
    public Unit save(Unit unit) {
        //新增
        if (EmptyUtil.isEmpty(unit.getId())) {
            unit.setId(UUIDUtil.getUUID());
            unit.setCreateTime(System.currentTimeMillis());
            unit.setCreatorId(UserUtil.getUserId());
        } else {
            //修改
            Unit old = (Unit) repository.findOne(unit.getId());
            unit.setCreateTime(old.getCreateTime());
            unit.setCreatorId(old.getCreatorId());
        }
        unit.setUpdateTime(System.currentTimeMillis());
        repository.save(unit);
        return unit;
    }


    /**
     * 修改组织机构
     */
    @Override
    public Map upd(UpdUnitDto updto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(updto.getId()));
        Update update = new Update();
        update.set(UNIT_NAME, updto.getName());
        update.set(UNIT_NUM, updto.getNum());
        update.set(UNIT_ABBRE, updto.getAbbre());
        update.set(UNIT_ADDRESS, updto.getAddress());
        update.set(UNIT_TELEPHONE, updto.getTelephone());
        update.set(UPDATE_TIME, System.currentTimeMillis());
        update.set(UNIT_PID, updto.getPid());
        update.set(DESC, updto.getDesc());
        template.updateMulti(query, update, Unit.class);
        //通过id去查询该实体展示给页面
        Unit unit = repository.findOne(updto.getId());
        return unit.toDto();


    }

    @Override
    public List<Unit> findFirstTree() {
        Query query = new Query();
        query.addCriteria(Criteria.where(UNIT_PID).is(null));
        return template.find(query, Unit.class);
    }

    @Override
    public List<Unit> findByPid(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.UNIT_PID).is(id));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.UNIT_NUM));
        return template.find(query, Unit.class);
    }

}
