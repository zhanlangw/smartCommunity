package cn.bytecloud.smartCommunity.role.dto;

import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;


@Data
public class RoleItemDto {

    private String id;
    /**
     * 角色名称
     */
    private String name;


    /**
     * 拥有权限集合
     */
    private Set<String> permissionIds;


    private String desc;


    /**
     * 关联摄像机ids
     */
    private Set<String> deviceIds;

    private long createTime;

    public String getCreateTime() {
        return StringUtil.getTime(new Date(createTime));
    }
}
