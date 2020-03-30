package cn.bytecloud.smartCommunity.user.dto;

import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserListDto {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String username;

    @Setter
    private List<String> unitIds;

    @Getter
    @Setter
    private UserType userType;

    public String getUnit() {
        UnitService unitService = SpringUtils.getBean(UnitService.class);
        StringBuilder name = new StringBuilder();
        for (String unitId : unitIds) {
            name.append(unitService.findById(unitId).getName()).append(" ");
        }
        return name.toString().trim().replaceAll(" ",",");
    }
}
