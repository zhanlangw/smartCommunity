package cn.bytecloud.smartCommunity.unit.dao;

import cn.bytecloud.smartCommunity.unit.entity.Unit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends MongoRepository<Unit, String> {

    /**
     * 查询组织机构集合
     *
     * @param pid:指定父亲节点id
     * @return 组织机构集合
     */
    List<Unit> findByPid(String pid);


    /**
     * 通过名字查询组织机构
     *
     * @param name:指定名字
     * @return Unit:组织机构
     */
    Unit findByName(String name);

    Unit findByAbbre(String abbre);
}
