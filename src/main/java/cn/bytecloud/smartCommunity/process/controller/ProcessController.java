package cn.bytecloud.smartCommunity.process.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.process.dto.AddProcessDto;
import cn.bytecloud.smartCommunity.process.dto.ProcessPageDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessStyleDto;
import cn.bytecloud.smartCommunity.process.service.ProcessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.process.controller.ProcessController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class ProcessController {
    public static final String API = "/api/process/";

    @Autowired
    private ProcessService service;

    @PostMapping(value = "add",name = "创建")
    @RequiresPermissions((API + "add"))
    @Menu("流程模板")
    public APIResult add(@RequestBody @Validated AddProcessDto dto) throws ByteException {
        return APIResult.success("创建成功").setValue(service.add(dto));
    }

    @GetMapping(value = "item",name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("流程模板")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success("创建成功").setValue(service.item(id));
    }

    @PostMapping(value = "upd",name = "修改基础信息")
    @RequiresPermissions((API + "upd"))
    @Menu("流程模板")
    public APIResult upd(@RequestBody @Validated UpdProcessDto dto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.upd(dto));
    }

    @GetMapping(value = "del",name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("流程模板")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success("删除成功");
    }

    @PostMapping(value = "updstyle",name = "修改流程图")
    @RequiresPermissions((API + "updstyle"))
    @Menu("流程模板")
    public APIResult updStyle(@Validated @RequestBody UpdProcessStyleDto dto){
        service.updStyle(dto);
        return APIResult.success("修改成功");
    }

    @GetMapping(value = "list",name = "列表")
    @RequiresPermissions((API + "list"))
    @Menu("流程模板")
    public APIResult list(@Validated ProcessPageDto dto){
        return APIResult.success("查询成功").setValue(service.list(dto));
    }

    @GetMapping(value = "ids",name = "id映射")
    public APIResult ids(@RequestParam String id){
        return APIResult.success("查询成功").setValue(service.ids(id));
    }


}
