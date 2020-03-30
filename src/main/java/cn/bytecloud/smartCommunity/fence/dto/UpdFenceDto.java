package cn.bytecloud.smartCommunity.fence.dto;

import cn.bytecloud.smartCommunity.fence.entity.Fence;
import lombok.Data;

@Data
public class UpdFenceDto extends AddFenceDto {
    private String id;

    @Override
    public Fence toData() {
        Fence fence = super.toData();
        fence.setId(id);
        return fence;
    }
}
