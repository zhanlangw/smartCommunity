package cn.bytecloud.smartCommunity.fence.service;

import cn.bytecloud.smartCommunity.fence.dto.AddFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.FenceItemDto;
import cn.bytecloud.smartCommunity.fence.dto.PageFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.UpdFenceDto;

public interface FenceService {
    FenceItemDto add(AddFenceDto dto) throws Exception;

    FenceItemDto upd(UpdFenceDto dto) throws Exception;

    FenceItemDto item(String id);

    void del(String ids) throws Exception;

    Object list(PageFenceDto dto);

    Object fenceItem(String deviceId);

    Object typeList();

}
