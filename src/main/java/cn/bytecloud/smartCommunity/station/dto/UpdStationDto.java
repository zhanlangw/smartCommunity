package cn.bytecloud.smartCommunity.station.dto;

import cn.bytecloud.smartCommunity.station.entity.Station;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdStationDto  extends AddStationDto{
    @NotEmpty
    private String id;

    @Override
    public Station toData() {
        Station station = super.toData();
        station.setId(id);
        return station;
    }
}
