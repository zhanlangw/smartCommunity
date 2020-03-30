package cn.bytecloud.smartCommunity.device.dto;

import cn.bytecloud.smartCommunity.device.entity.DevicFeatuers;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.device.entity.DeviceType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.DEVICE_LONGITUDE;
@Data
public class RpcDto {
    private String id;
    //名字
    private String name;

    private String vendor;

    //ip
    private String ip;

    //端口
    private Integer port;

    //url
    private String url;

    //用户名
    private String username;

    //密码
    private String password;

}
