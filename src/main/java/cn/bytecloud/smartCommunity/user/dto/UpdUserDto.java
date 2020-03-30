package cn.bytecloud.smartCommunity.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 个人中心修改用Dto
 */
@Data
public class UpdUserDto {

    @NotEmpty
    private String id;

    private String imagePath;

    @Length(min = 2, max = 20)
    @NotEmpty
    private String name;

    @NotEmpty
    private String gender;

    @Length(max = 20)
    private String birthday;

    private String age;

    private String telephone;

    private String email;

    @Length(max = 100)
    private String address;


}
