package cn.bytecloud.smartCommunity.fence.dto;

import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FenceItemDto {

    private String id;

    private String name;

    private List<Integer> num;

    private List<String> type;

    private String desc;

    private String deviceId;

    private long createTime;
    private long updateTime;
    public String getCreateTime() {
        return StringUtil.getTime(new Date(createTime));
    }

    public String getUpdateTime() {
        return StringUtil.getTime(new Date(updateTime));
    }

    public String getDeviceName(){
        Device device = SpringUtils.getBean(DeviceService.class).findById(deviceId);
        return device == null ? "" : device.getName();
    }
}
