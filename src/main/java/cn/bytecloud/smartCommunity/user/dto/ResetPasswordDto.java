package cn.bytecloud.smartCommunity.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 重置密码用Dto
 */


@Data
public class ResetPasswordDto {

    @NotEmpty
    private String id;

    @NotEmpty
    @Length(min = 6, max = 20)
    private String password;


}
