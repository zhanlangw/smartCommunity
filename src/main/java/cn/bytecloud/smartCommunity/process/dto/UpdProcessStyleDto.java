package cn.bytecloud.smartCommunity.process.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdProcessStyleDto {
    @NotEmpty
    @Length(max = 40)
    private String id;

    @NotEmpty
    private String style;
}
