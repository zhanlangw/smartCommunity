package cn.bytecloud.smartCommunity.Basis.dao;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BasosDaoImpl extends BaseDao<Basis> implements BasisDao {
    @Autowired
    private BasisRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public Basis findFirst() {
       return template.findOne(new Query(), Basis.class);
    }
}
