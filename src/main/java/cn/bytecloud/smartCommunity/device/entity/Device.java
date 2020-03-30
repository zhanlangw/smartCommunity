package cn.bytecloud.smartCommunity.device.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection =T_DEVICE)
public class Device extends BaseEntity {
    //名字
    @Field(DEVICE_NAME)
    private String name;

    //供应商
    @Field(DEVICE_VENDOR)
    private String vendor;

    //编号
    @Field(DEVICE_NUM)
    private String num;

    //地址
    @Field(DEVICE_ADDRESS)
    private String address;

    //组织机构
    @Field(DEVICE_UNIT_ID)
    private String unitId;

    //类型
    @Field(DEVICE_TYPE)
    private DeviceType type;

    //功能
    @Field(DEVICE_FEATURES)
    private DevicFeatuers features;

    //ip
    @Field(DEVICE_IP)
    private String ip;

    //端口
    @Field(DEVICE_TCP_PORT)
    private Integer tcpPort;

    //状态
    @Field(DEVICE_STATUS)
    private DeviceStatus status;

    //协议
    @Field(DEVICE_RTSP_ADDRESS)
    private String rtspAddress;

    //url
    @Field(DEVICE_RTSP_PORT)
    private String rtspPort;

    //用户名
    @Field(DEVICE_USERNAME)
    private String username;

    //密码
    @Field(DEVICE_PASSWORD)
    private String password;

    //维度
    @Field(DEVICE_LATITUDE)
    private String latitude;

    //经度
    @Field(DEVICE_LONGITUDE)
    private String longitude;
}
