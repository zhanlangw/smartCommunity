package cn.bytecloud.smartCommunity.device.dto;

import cn.bytecloud.smartCommunity.device.entity.DevicFeatuers;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.device.entity.DeviceType;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.DEVICE_VENDOR;

public class DeviceItemDto {

    @Getter
    @Setter
    private String id;

    //名字
    @Getter
    @Setter
    private String name;

    //编号
    @Getter
    @Setter
    private String num;

    //地址
    @Getter
    @Setter
    private String address;

    //组织机构
    @Setter
    private String unitId;

    //类型
    @Getter
    @Setter
    private DeviceType type;

    //功能
    @Getter
    @Setter
    private DevicFeatuers features;

    //ip
    @Getter
    @Setter
    private String ip;

    //端口
    @Getter
    @Setter
    private Integer tcpPort;

    //状态
    @Getter
    @Setter
    private DeviceStatus status;

    //协议
    @Getter
    @Setter
    private String rtspAddress;

    //url
    @Getter
    @Setter
    private String rtspPort;

    //用户名
    @Getter
    @Setter
    private String username;

    //密码
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String vendor;

    //维度
    @Getter
    @Setter
    private String latitude;

    //维度
    @Getter
    @Setter
    private String longitude;

    public Map getUnit() {
        Map map = new HashedMap();
        map.put("id", unitId);
        map.put("name", SpringUtils.getBean(UnitService.class).findById(unitId).getName());
        return map;
    }
}
