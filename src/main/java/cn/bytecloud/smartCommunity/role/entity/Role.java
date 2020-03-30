package cn.bytecloud.smartCommunity.role.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;


/**
 * role 角色实体
 */
@Data
@Document(collection = T_ROLE)
public class Role extends BaseEntity {

    /**
     * 角色名称
     */
    @Field(ROLE_NAME)
    private String name;

    /**
     * 排序编号
     */
    @Field(ROLE_NUM)
    private Integer num;

    /**
     * 拥有权限集合
     */
    @Field(ROLE_PERMISSIONS)
    private Set<String> permissionIds;


    /**
     * 关联摄像机ids
     */
    @Field(ROLE_DEVICE_IDS)
    private Set<String> deviceIds;


}
