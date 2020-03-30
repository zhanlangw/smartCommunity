package cn.bytecloud.smartCommunity.device.dao;

import cn.bytecloud.smartCommunity.device.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device,String> {
    Device findFirstByName(String name);

    Device findFirstByNum(String num);

    List<Device> findByUnitId(String id);
}
