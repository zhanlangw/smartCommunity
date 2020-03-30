package cn.bytecloud.smartCommunity.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 */
@Data
public class LoginDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotNull
    private Integer type;

}
