package cn.bytecloud.smartCommunity.userAddress.dao;

import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.userAddress.dto.UserAddressPageDto;
import cn.bytecloud.smartCommunity.userAddress.entity.UserAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface UserAddressDao {
    List<UserAddress> list(UserAddressPageDto dto);

    List<HashMap> supervision(Set<String> collect);

    UserAddress findOneByUserId();

    long onlineCount(long time);

    long onlineCountByIds(long time, Set<String> userIds);
}
