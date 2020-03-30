package cn.bytecloud.smartCommunity.role.dto;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.permission.service.PermissionService;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 新建角色用Dto
 */
@Data
public class AddRoleDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    @Length(max = 100)
    private String desc;

    @NotEmpty
    private Set<String> permissionIds = new HashSet<>();
    @NotEmpty
    private Set<String> deviceIds;

//    public Set<String> getPermissionIds() {
//        Set<String> data = SpringUtils.getBean(PermissionService.class).findAll().stream().map(BaseEntity::getId).collect(Collectors.toSet());
//        return permissionIds.stream().filter(data::contains).collect(Collectors.toSet());
//    }

    public Role toData() {
        Role role = new Role();
        role.setName(name);
        role.setPermissionIds(permissionIds);
        role.setDeviceIds(deviceIds);
        role.setDesc(desc);
        return role;
    }
}
