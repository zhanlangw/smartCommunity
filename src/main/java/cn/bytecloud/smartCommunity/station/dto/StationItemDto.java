package cn.bytecloud.smartCommunity.station.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
@Data
public class StationItemDto {
    private String id;
    //名字
    private String name;

    //地址
    private String address;

    //维度
    private String latitude;

    //维度
    private String longitude;

    private String desc;
}
