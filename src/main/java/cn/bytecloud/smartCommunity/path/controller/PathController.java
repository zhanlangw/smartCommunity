package cn.bytecloud.smartCommunity.path.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.dto.AddNodeDto;
import cn.bytecloud.smartCommunity.node.dto.UpdNodeDto;
import cn.bytecloud.smartCommunity.path.dto.AddPathDto;
import cn.bytecloud.smartCommunity.path.dto.UpdPathDto;
import cn.bytecloud.smartCommunity.path.service.PathService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.path.controller.PathController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class PathController {
    public static final String API = "/api/path/";

    @Autowired
    private PathService service;

    @PostMapping(value = "add", name = "创建")
    @RequiresPermissions((API + "add"))
    @Menu("路径")
    public APIResult add(@RequestBody @Validated AddPathDto dto) throws ByteException {
        return APIResult.success("创建成功").setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("路径")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success("请求成功").setValue(service.item(id));
    }

    @PostMapping(value = "upd",name = "修改基础信息")
    @RequiresPermissions((API + "upd"))
    @Menu("路径")
    public APIResult upd(@RequestBody @Validated UpdPathDto dto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.add(dto));
    }

    @GetMapping(value = "del",name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("路径")
    public APIResult del(@RequestParam String ids){
        service.del(ids);
        return APIResult.success("删除成功");
    }
}
