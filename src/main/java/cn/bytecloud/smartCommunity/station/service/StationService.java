package cn.bytecloud.smartCommunity.station.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dto.AddSmallCategoryDto;
import cn.bytecloud.smartCommunity.station.dto.AddStationDto;
import cn.bytecloud.smartCommunity.station.dto.StationItemDto;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.dto.UpdStationDto;

public interface StationService {
    StationItemDto add(AddStationDto dto) throws ByteException;

    StationItemDto item(String id);

    StationItemDto upd(UpdStationDto dto) throws ByteException;

    void del(String id);

    Object list(StationPageDto dto);

    Object mapList();
}
