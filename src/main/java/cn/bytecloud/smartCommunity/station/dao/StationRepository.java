package cn.bytecloud.smartCommunity.station.dao;

import cn.bytecloud.smartCommunity.station.entity.Station;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends MongoRepository<Station,String>{
    List<Station> findByName(String name);
}
