package cn.bytecloud.smartCommunity.address.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.WORK_LATITUDE;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.WORK_LONGITUDE;

@Data
public class ImportAddressDto {
    //维度
    private String latitude;

    //维度
    private String longitude;

}
