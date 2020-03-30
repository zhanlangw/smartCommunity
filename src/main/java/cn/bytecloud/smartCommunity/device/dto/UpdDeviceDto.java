package cn.bytecloud.smartCommunity.device.dto;

import cn.bytecloud.smartCommunity.device.entity.Device;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdDeviceDto extends AddDeviceDto{

    @NotEmpty
    private String id;

    @Override
    public Device toData() {
        Device device = super.toData();
        device.setId(id);
        return device;
    }
}
