package cn.bytecloud.smartCommunity.station.dao;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.entity.Station;

public interface StationDao {
    Station save(Station station);

    PageModel<Station> list(StationPageDto dto);
}
