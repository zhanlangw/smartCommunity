package cn.bytecloud.smartCommunity.Basis.dao;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasisRepository  extends MongoRepository<Basis,String>{
}
