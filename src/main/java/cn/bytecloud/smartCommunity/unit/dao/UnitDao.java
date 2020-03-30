package cn.bytecloud.smartCommunity.unit.dao;

import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UnitDao {

    Unit save(Unit unit);

    Map upd(UpdUnitDto dto);

    List<Unit> findFirstTree();

    List<Unit> findByPid(String id);
}
