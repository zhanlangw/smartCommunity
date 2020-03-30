package cn.bytecloud.smartCommunity.device.dto;

import cn.bytecloud.smartCommunity.device.entity.DevicFeatuers;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.device.entity.DeviceType;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
public class AddDeviceDto {

    @NotEmpty
    @Length(max = 20)
    private String vendor;

    //名字
    @NotEmpty
    @Length(max = 20)
    private String name;

    //编号
    @NotEmpty
    @Length(max = 20)
    private String num;

    //地址
    @NotEmpty
    @Length(max = 100)
    private String address;

    //组织机构
    @NotEmpty
    private String unitId;

    //类型
    @NotNull
    private DeviceType type;

    //功能
    @NotNull
    private DevicFeatuers features;

    //ip
    @NotEmpty
    @Length(max = 20)
    private String ip;

    //端口
    @NotNull
    @Max(65535)
    @Min(1)
    private Integer tcpPort;

    //状态
    @NotNull
    private DeviceStatus status;

    //维度
    @NotEmpty
    @Length(max = 20)
    private String latitude;

    //维度
    @NotEmpty
    @Length(max = 20)
    private String longitude;

    //协议
    private String rtspAddress;

    //url
    @NotEmpty
    @Length(max = 100)
    private String rtspPort;

    //用户名
    @NotEmpty
    @Length(max = 50)
    private String username;

    //密码
    @NotEmpty
    @Length(max = 50)
    private String password;

    public Device toData(){
        return EntityUtil.entityToDto(this, Device.class);
    }
}
