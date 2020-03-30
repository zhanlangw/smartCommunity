package cn.bytecloud.smartCommunity.work.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

@Data
public class AddWorkFromDeviceDto implements Serializable {
    @NotEmpty
    private String deviceId;

    @NotEmpty
    private String blacklistId;

    @NotEmpty
    private String imageBase64Str;

    @NotEmpty
    private String eventId;

    private Boolean flag;
}

