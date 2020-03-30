package cn.bytecloud.smartCommunity.permission.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionTree {
    private String id;

    private String name;

    private Integer type; //1为菜单,2为权限

    private List<PermissionTree> tree = new ArrayList<>();
}
