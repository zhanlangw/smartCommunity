package cn.bytecloud.smartCommunity.station.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 站点
 */
@Data
@Document(collection = T_STATION)
public class Station extends BaseEntity {
    //名字
    @Field(STATION_NAME)
    private String name;

    //地址
    @Field(STATION_ADDRESS)
    private String address;

    //维度
    @Field(STATION_LATITUDE)
    private String latitude;

    //维度
    @Field(STATION_LONGITUDE)
    private String longitude;
}
