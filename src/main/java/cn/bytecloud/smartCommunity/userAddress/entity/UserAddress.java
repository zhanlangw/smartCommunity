package cn.bytecloud.smartCommunity.userAddress.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection = ModelConstant.T_USER_ADDRESS)
public class UserAddress extends BaseEntity{
    //维度
    @Field(USER_ADDRESS_LATITUDE)
    private Double latitude;

    //经度
    @Field(USER_ADDRESS_LONGITUDE)
    private Double longitude;

    @Field(USER_ADDRESS_ONLINE_FLAG)
    private Boolean onlineFlag;
}
