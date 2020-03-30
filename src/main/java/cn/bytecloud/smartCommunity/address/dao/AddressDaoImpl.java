package cn.bytecloud.smartCommunity.address.dao;

import cn.bytecloud.smartCommunity.address.entity.Address;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressDaoImpl implements AddressDao  {
    @Autowired
    private AddressRepository repository;

    @Autowired
    private MongoTemplate template;

    @Override
    public List<Address> findByNum(int num) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ADDRESS_NUM).is(num));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.CREATE_TIME));
        return template.find(query, Address.class);
    }
}
