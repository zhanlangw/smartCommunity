package cn.bytecloud.smartCommunity.video.realpaly.dto;


import cn.bytecloud.smartCommunity.device.entity.DevicFeatuers;
import cn.bytecloud.smartCommunity.device.entity.Device;
import lombok.Getter;
import lombok.Setter;

public class RealPlayDto {

    //推流地址(标清)
    @Getter
    @Setter
    private String address;

    //推流地址(高清)
    @Getter
    @Setter
    private String highAddress;

    //摄像机id
    @Getter
    @Setter
    private String id;

    //摄像机名字
    @Getter
    @Setter
    private String name;

    //摄像机类型
    @Getter
    @Setter
    private String type;

    //摄像机编号
    @Getter
    @Setter
    private String num;

    //关联街区
    @Getter
    @Setter
    private String unitName;

    //功能
    @Setter
    @Getter
    private DevicFeatuers features;

}
