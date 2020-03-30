package cn.bytecloud.smartCommunity.user.dto;

import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改用UserDto
 */
@Data
public class UpdUserInUnitDto extends AddUserInUnitDto {


    @NotEmpty
    private String id;

    @Override
    public User toEntity() {
        User user = super.toEntity();
        user.setId(id);
        return user;
    }
}
