package cn.bytecloud.smartCommunity.user.dto;


import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.MD5Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * 通过组织机构新增用户Dto
 */
@Data
public class AddUserInUnitDto {


    /**
     * 姓名
     */
    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotNull
    private Integer num;


    @NotNull
    private UserType userType;

    /**
     * 拥有角色集合
     */
    @NotEmpty
    private Set<String> roleIds;

    /**
     * 办公地点
     */
    @Length(max = 100)
    private String address;

    /**
     * 联系电话
     */
    @Length(max = 11)
    private String telephone;

    /**
     * 关联组织机构id
     */
    @NotEmpty
    private List<String> unitIds;

    /**
     * 登录帐号
     */
    @NotEmpty
    @Length(max = 20)
    private String username;


    /**
     * 备注
     */
    @Length(max = 100)
    private String desc;


    public User toEntity() {
        User user = new User();
        user.setNum(num);
        user.setUserType(userType);
        user.setUsername(username);
        user.setRoleIds(roleIds);
        user.setAddress(address);
        user.setName(name);
        user.setTelephone(telephone);
        user.setUnitIds(unitIds);
        user.setDesc(desc);
        user.setPassword(MD5Util.getMD5(SystemConstant.DEFUALT_PASSWORD));
        return user;
    }


}
