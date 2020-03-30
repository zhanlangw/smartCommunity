package cn.bytecloud.smartCommunity.device.dao;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.device.dto.DevicePageDto;
import cn.bytecloud.smartCommunity.device.dto.PageAppListDto;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface DeviceDao {
    Device save(Device device);

    PageModel<HashMap> list(DevicePageDto dto);

    PageModel<Device> rpcList(BasePageDto dto);

    List<Device> mapList();

    PageModel<HashMap> appList(PageAppListDto dto, Set<String> deviceIds);

    long countByOnline(DeviceStatus online);

    List<Device> findByName(String title);
}
