package cn.bytecloud.smartCommunity.address.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = T_ADDRESS)
public class Address {
    @Id
    private String id;

    @Field(ADDRESS_NUM)
    private Integer num;

    //维度
    @Field(ADDRESS_LATITUDE)
    private String latitude;

    //经度
    @Field(ADDRESS_LONGITUDE)
    private String longitude;

    //组织机构id
    @Field(ADDRESS_UNIT)
    private String unitId;

    @Field(CREATE_TIME)
    private long createTime;
}
