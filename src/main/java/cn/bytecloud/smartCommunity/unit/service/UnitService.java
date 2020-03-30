package cn.bytecloud.smartCommunity.unit.service;

import cn.bytecloud.smartCommunity.base.dto.TreeDto;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.unit.dto.UnitDto;
import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;

import java.util.List;
import java.util.Map;

public interface UnitService {

    Map add(UnitDto dto) throws ByteException;

    Map item(String id);

    void del(String id) throws ByteException;

    Map upd(UpdUnitDto updto) throws ByteException;

    List<TreeDto> tree(String id, boolean b);

    String findNameById(String id);

    Unit findByName(String name);

    Unit save(Unit save);

    Unit findById(String unitId);

    Object deivceTree();

    List<Unit> findByPid(String pid);

    Object appTree(String id);

    List<String> findAllByPid(String unitId);

    List<Unit> findAll();
}
