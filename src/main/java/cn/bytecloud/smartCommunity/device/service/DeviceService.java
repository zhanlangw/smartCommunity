package cn.bytecloud.smartCommunity.device.service;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.device.dto.*;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.exception.ByteException;

import java.util.List;

public interface DeviceService {
    DeviceItemDto add(AddDeviceDto dto) throws ByteException;

    DeviceItemDto item(String id);

    DeviceItemDto upd(UpdDeviceDto dto) throws ByteException;

    void del(String id) throws ByteException;

    PageModel list(DevicePageDto dto);

    List<Device> findByUnitId(String id);

    Object rpcList(BasePageDto dto);

    Device findById(String deviceId);

    Object mapList();

    Object appList(PageAppListDto dto);

    long count();

    long countByOnline();

    List<Device> findAll();

    List<Device> findByName(String title);
}
