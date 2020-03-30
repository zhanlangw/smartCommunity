package cn.bytecloud.smartCommunity.station.dto;

import cn.bytecloud.smartCommunity.station.entity.Station;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
public class AddStationDto {
    //名字
    @NotEmpty
    @Length(max = 20)
    private String name;

    //地址
    @NotEmpty
    @Length(max = 100)
    private String address;

    //维度
    @NotEmpty
    @Length(max = 20)
    private String latitude;

    @Length(max = 100)
    private String desc;

    //维度
    @NotEmpty
    @Length(max = 20)
    private String longitude;

    public Station toData(){
        Station station = new Station();
        station.setName(name);
        station.setAddress(address);
        station.setLatitude(latitude);
        station.setLongitude(longitude);
        station.setDesc(desc);
        return station;
    }
}
