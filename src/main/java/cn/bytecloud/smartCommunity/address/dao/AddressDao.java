package cn.bytecloud.smartCommunity.address.dao;

import cn.bytecloud.smartCommunity.address.entity.Address;

import java.util.List;

public interface AddressDao {
    List<Address> findByNum(int num);
}
