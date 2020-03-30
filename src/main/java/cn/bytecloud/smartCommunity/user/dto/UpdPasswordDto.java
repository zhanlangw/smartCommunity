package cn.bytecloud.smartCommunity.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdPasswordDto {
    private String password;

    @NotEmpty
    @Length(max = 20,min = 6)
    private String newPasswrod;
}
