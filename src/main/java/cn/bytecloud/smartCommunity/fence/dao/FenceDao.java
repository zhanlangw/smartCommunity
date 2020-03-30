package cn.bytecloud.smartCommunity.fence.dao;

import cn.bytecloud.smartCommunity.fence.dto.PageFenceDto;
import cn.bytecloud.smartCommunity.fence.entity.Fence;

import java.util.Set;

public interface FenceDao  {
    Fence save(Fence fence);

    void del(String ids);

    Object list(PageFenceDto dto,Set<String> deviceIds);
}
