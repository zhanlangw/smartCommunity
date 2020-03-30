package cn.bytecloud.smartCommunity.blacklist.service;

import cn.bytecloud.smartCommunity.blacklist.dto.AddBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistItemDto;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistPageDto;
import cn.bytecloud.smartCommunity.blacklist.dto.UpdBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.exception.ByteException;

import java.util.List;


public interface BlacklistService {
    BlacklistItemDto add(AddBlacklistDto dto) throws Exception;

    BlacklistItemDto item(String id);

    BlacklistItemDto upd(UpdBlacklistDto dto) throws Exception;

    void del(String id) throws ByteException;

    Object list(BlacklistPageDto dto);

    void init();

    Blacklist findById(String blacklistId);

    List<Blacklist> findByTypeId(String typeId);
}
