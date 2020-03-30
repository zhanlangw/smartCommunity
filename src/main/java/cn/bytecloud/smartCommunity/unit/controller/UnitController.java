package cn.bytecloud.smartCommunity.unit.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.unit.dto.UnitDto;
import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.unit.controller.UnitController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class UnitController {

    public static final String API = "/api/unit/";

    @Autowired
    private UnitService service;

    @PostMapping(value = "add", name = "新建")
    @RequiresPermissions((API + "add"))
    @Menu("组织机构")
    public APIResult add(@RequestBody @Validated UnitDto dto) throws ByteException {
        return APIResult.success("创建成功").setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("组织机构")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success("查询成功").setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions((API + "upd"))
    @Menu("组织机构")
    public APIResult upd(@RequestBody @Validated UpdUnitDto updto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.upd(updto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("组织机构")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success("删除成功");
    }

    @GetMapping(value = "tree", name = "树状图(组织机构)")
    @RequiresPermissions((API + "tree"))
    @Menu("组织机构")
    public APIResult tree(String id) {
        return APIResult.success("树形结构查询成功").setValue(service.tree(id,false));
    }

    @GetMapping(value = "user/tree", name = "树状图(组织机构加用户)")
    @RequiresPermissions((API + "user/tree"))
    @Menu("组织机构")
    public APIResult userTree(String id) {
        return APIResult.success("树形结构查询成功").setValue(service.tree(id, true));
    }

    /**
     * app树状图(组织机构加用户
     * @param id
     * @return
     */
    @GetMapping(value = "app/user/tree")
    @RequiresPermissions((API + "user/tree"))
    public APIResult appUserTree(String id) {
        return APIResult.success("树形结构查询成功").setValue(service.appTree(id));
    }
}
