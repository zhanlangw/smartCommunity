package cn.bytecloud.smartCommunity.node.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.dto.AddNodeDto;
import cn.bytecloud.smartCommunity.node.dto.UpdNodeDto;
import cn.bytecloud.smartCommunity.node.service.NodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.node.controller.NodeController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class NodeController {
    public static final String API = "/api/node/";

    @Autowired
    private NodeService service;

    @PostMapping(value = "add", name = "创建")
    @RequiresPermissions((API + "add"))
    @Menu("环节")
    public APIResult add(@RequestBody @Validated AddNodeDto dto) throws ByteException {
        return APIResult.success("创建成功").setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("环节")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success("创建成功").setValue(service.item(id));
    }

    @PostMapping(value = "upd",name = "修改基础信息")
    @RequiresPermissions((API + "upd"))
    @Menu("环节")
    public APIResult upd(@RequestBody @Validated UpdNodeDto dto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.add(dto));
    }

    @GetMapping(value = "del",name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("环节")
    public APIResult del(@RequestParam String ids){
        service.del(ids);
        return APIResult.success("删除成功");
    }
}
