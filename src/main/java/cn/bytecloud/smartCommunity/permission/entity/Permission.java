package cn.bytecloud.smartCommunity.permission.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * Permission 权限实体
 */
@Data
@Document(collection = T_PERMISSION)
public class Permission extends BaseEntity {

    /**
     * 权限名称
     */
    @Field(PERMISSION_NAME)
    private String name;

    /**
     * API路径
     */
    @Field(PERMISSION_URL)
    private String interfaceUrl;

    /**
     * 关联菜单id
     */
    @Field(PERMISSION_MENU_ID)
    private String menuId;

}
