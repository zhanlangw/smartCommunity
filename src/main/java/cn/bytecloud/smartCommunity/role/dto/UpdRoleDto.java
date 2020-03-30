package cn.bytecloud.smartCommunity.role.dto;

import cn.bytecloud.smartCommunity.role.entity.Role;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 版权 @Copyright: 2019 www.bytecloud.cn Inc. All rights reserved.
 * 文件名称： UpdRoleDto
 * 包名：cn.bytecloud.smartCommunity.role.dto
 * 创建人：@author wangkn@bytecloud.cn
 * 创建时间：2019/07/05 16:57
 * 修改人：wangkn@bytecloud.cn
 * 修改时间：2019/07/05 16:57
 * 修改备注：
 */
@Data
public class UpdRoleDto extends AddRoleDto {

    @NotEmpty
    private String id;

    @Override
    public Role toData() {
        Role role = super.toData();
        role.setId(id);
        return role;
    }
}
