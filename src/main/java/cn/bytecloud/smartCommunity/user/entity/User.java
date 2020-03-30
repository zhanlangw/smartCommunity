package cn.bytecloud.smartCommunity.user.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.user.dto.UpdUserDto;
import cn.bytecloud.smartCommunity.user.dto.UserItemDto;
import cn.bytecloud.smartCommunity.user.dto.UserItemInUnitDto;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.*;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 用户
 */
@Data
@Document(collection = T_USER)
public class User extends BaseEntity {
    public User() {
        this.userflag = true;
    }


    /**
     * 登陆名
     */
    @Field(USER_USERNAME)
    private String username;

    @Field(USER_NUM)
    private Integer num;

    /**
     * 用户名
     */
    @Field(USER_NAME)
    private String name;

    /**
     * 头像存储地址
     */
    @Field(USER_IMAGE_PATH)
    private String imagePath;

    /**
     * 密码
     */
    @Field(USER_PASSWORD)
    private String password;

    /**
     * 是否有效,true:有效,false:失效
     */
    @Field(USER_USER_FLAG)
    private boolean userflag;

    /**
     * 电话号码
     */
    @Field(USER_TELEPHONE)
    private String telephone;

    /**
     * 性别
     */
    @Field(USER_GENDER)
    private String gender;

    /**
     * 生日
     */
    @Field(USER_BIRTHDAY)
    private String birthday;

    /**
     * 年龄
     */
    @Field(USER_AGE)
    private String age;

    /**
     * 邮箱
     */
    @Field(USER_EMAIL)
    private String email;

    /**
     * 家庭住址
     */
    @Field(USER_ADDRESS)
    private String address;

    /**
     * 关联组织机构ID
     */
    @Field(USER_UNIT_ID)
    private List<String> unitIds;

    /**
     * 拥有角色集合
     */
    @Field(USER_ROLE_IDS)
    private Set<String> roleIds = new HashSet<>();

    //用户类型
    @Field(USER_TYPE)
    private UserType userType;

    //是否开启告警弹框提醒
    @Field(USER_SOUND_FLAG)
    private Boolean soundFlag;


    /**
     * entity 转 UserItemInUnitDto:组织机构中展示的Dto
     */
    public UserItemInUnitDto toItemInUnitDto() {
        UserItemInUnitDto userItemInUnitDto = new UserItemInUnitDto();
        userItemInUnitDto.setId(super.getId());
        userItemInUnitDto.setUsername(username);
        userItemInUnitDto.setUserType(userType);
        userItemInUnitDto.setName(name);
        userItemInUnitDto.setNum(num);
        userItemInUnitDto.setRoleIds(roleIds);
        userItemInUnitDto.setAddress(address);
        userItemInUnitDto.setTelephone(telephone);
        userItemInUnitDto.setUnitIds(unitIds);
        userItemInUnitDto.setDesc(super.getDesc());
        return userItemInUnitDto;
    }

    /**
     * entity 转 UserItemDto:个人中心展示的Dto
     */
    public UserItemDto toItemDto() {
        UserItemDto userItemDto = new UserItemDto();
        userItemDto.setUserType(userType);
        userItemDto.setId(super.getId());
        userItemDto.setImagePath(imagePath);
        userItemDto.setName(name);
        userItemDto.setGender(gender);
        userItemDto.setBirthday(birthday);
        userItemDto.setAge(age);
        userItemDto.setTelephone(telephone);
        userItemDto.setEmail(email);
        userItemDto.setAddress(address);
        userItemDto.setUnitIds(unitIds);
        userItemDto.setRoleIds(roleIds);
        return userItemDto;
    }
}


