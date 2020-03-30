package cn.bytecloud.smartCommunity.userAddress.service;

import cn.bytecloud.smartCommunity.userAddress.dto.UserAddressPageDto;

import java.util.Set;

public interface UserAddressService {
    void add(Double longitude, Double latitude);

    Object list(UserAddressPageDto userId);

    Object supervision();

    long onlineCount(long time);

    long onlineCountByIds(long time, Set<String> userIds);
}
