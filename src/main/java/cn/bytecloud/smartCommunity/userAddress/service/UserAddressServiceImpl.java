package cn.bytecloud.smartCommunity.userAddress.service;

import cn.bytecloud.smartCommunity.address.service.AddressService;
import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.userAddress.dao.UserAddressDao;
import cn.bytecloud.smartCommunity.userAddress.dao.UserAddressRepository;
import cn.bytecloud.smartCommunity.userAddress.dto.UserAddressPageDto;
import cn.bytecloud.smartCommunity.userAddress.entity.UserAddress;
import cn.bytecloud.smartCommunity.util.AddressUtil;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressRepository repository;
    @Autowired
    private UserAddressDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Override
    public void add(Double longitude, Double latitude) {
        UserAddress address = dao.findOneByUserId();

        if (address != null && AddressUtil.getDistance(latitude, longitude, address.getLatitude(), address.getLongitude()) < 10) {
            address.setUpdateTime(System.currentTimeMillis());
            repository.save(address);
            return;
        }


        UserAddress userAddress = new UserAddress();
        userAddress.setLatitude(latitude);
        userAddress.setLongitude(longitude);
        userAddress.setCreatorId(UserUtil.getUserId());
        userAddress.setCreateTime(System.currentTimeMillis());
        userAddress.setId(UUIDUtil.getUUID());

        try {
            addressService.findUnitIdByLatitudeAndLongitude(latitude + "", longitude + "");
            userAddress.setOnlineFlag(true);
        } catch (ByteException e) {
            userAddress.setOnlineFlag(false);
        }

        repository.save(userAddress);
    }

    @Override
    public Object list(UserAddressPageDto dto) {
        List<Map> data = new ArrayList<>();
        for (UserAddress userAddress : dao.list(dto)) {
            Map map = new HashedMap();
            map.put("latitude", userAddress.getLatitude());
            map.put("longitude", userAddress.getLongitude());
            data.add(map);
        }
        return data;
    }

    @Override
    public Object supervision() {
        Map<String, User> userMap = new HashedMap<>();
        List<User> users = userService.findByUserType(UserType.WORKER);
        List<HashMap> data = dao.supervision(users.stream().peek(user -> userMap.put(user.getId(), user)).map(BaseEntity::getId).collect(Collectors.toSet()));
        data.forEach(item -> {
            Object id = item.remove("_id");
            User user = userMap.get(id);
            item.put("username", user.getName());
            item.put("id", id);
            item.put("telephone", user.getTelephone());
            UnitService unitService = SpringUtils.getBean(UnitService.class);
            StringBuilder name = new StringBuilder();
            for (String unitId : user.getUnitIds()) {
                name.append(unitService.findById(unitId).getName()).append(" ");
            }
            item.put("unit", name.toString().trim().replaceAll(" ",","));
        });
        return data;
    }

    @Override
    public long onlineCount(long time) {
        return dao.onlineCount(time);
    }

    @Override
    public long onlineCountByIds(long time, Set<String> userIds) {
        return dao.onlineCountByIds(time, userIds);
    }

}