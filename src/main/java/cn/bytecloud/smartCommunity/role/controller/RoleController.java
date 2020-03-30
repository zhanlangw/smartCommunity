package cn.bytecloud.smartCommunity.role.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.role.dto.AddRoleDto;
import cn.bytecloud.smartCommunity.role.dto.PageRoleDto;
import cn.bytecloud.smartCommunity.role.dto.UpdRoleDto;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.role.controller.RoleController.API;

/**
 */
@RestController
@RequestMapping(API)
@Menu("高级管理")
public class RoleController {

    public static final String API = "/api/role/";

    @Autowired
    private RoleService service;

    @PostMapping(value = "add", name = "新建")
    @RequiresPermissions((API + "add"))
    @Menu("角色")
    public APIResult add(@RequestBody @Validated AddRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("角色")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions((API + "upd"))
    @Menu("角色")
    public APIResult upd(@RequestBody @Validated UpdRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("角色")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "列表")
    @RequiresPermissions((API + "list"))
    @Menu("角色")
    public APIResult list(@Validated PageRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping(value = "permission/tree", name = "权限树")
    public APIResult permissionTree() throws ByteException {
        return APIResult.success().setValue(service.permissionTree());
    }

    @GetMapping(value = "device/tree", name = "摄像机树")
    public APIResult deivceTree() throws ByteException {
        return APIResult.success().setValue(service.deivceTree());
    }
}
