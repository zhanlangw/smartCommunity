package cn.bytecloud.smartCommunity.fence.dao;

import cn.bytecloud.smartCommunity.fence.entity.Fence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FenceRepository extends MongoRepository<Fence,String> {
    List<Fence> findByNameAndDeviceId(String name, String deviceId);

    List<Fence> findByDeviceId(String deviceId);
}
