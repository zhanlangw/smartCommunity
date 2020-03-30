package cn.bytecloud.smartCommunity.blacklist.dao;

import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistPageDto;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;

public interface BlacklistDao {
    Blacklist save(Blacklist blacklist, boolean flag);

    Object list(BlacklistPageDto dto);
}
