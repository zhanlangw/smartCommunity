package cn.bytecloud.smartCommunity.fence.dto;

import cn.bytecloud.smartCommunity.fence.entity.Fence;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AddFenceDto {

    @NotEmpty
    private String name;

    @NotNull
    @Size(min = 1,max = 144)
    private List<Integer> num;

    @NotNull
    @Size(min = 1)
    private List<String> type;


    private String desc;

    @NotEmpty
    private String deviceId;

    public Fence toData(){
        Fence fence = new Fence();
        fence.setName(name);
        fence.setNum(num);
        fence.setType(type);
        fence.setDesc(desc);
        fence.setDeviceId(deviceId);
        return fence;
    }
}
