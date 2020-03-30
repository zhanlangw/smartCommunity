package cn.bytecloud.smartCommunity.fence.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.fence.dto.AddFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.PageFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.UpdFenceDto;
import cn.bytecloud.smartCommunity.fence.service.FenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static cn.bytecloud.smartCommunity.fence.controller.FenceController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class FenceController implements Cloneable{

    public static final String API = "/api/fence/";

    @Autowired
    private FenceService service;


    @PostMapping(value = "add", name = "添加围栏")
    @RequiresPermissions(API + "add")
    @Menu("电子围栏")
    public APIResult add(@RequestBody @Validated AddFenceDto dto) throws Exception {
        return APIResult.success().setValue(service.add(dto));
    }

    @PostMapping(value = "upd", name = "修改围栏")
    @RequiresPermissions(API + "upd")
    @Menu("电子围栏")
    public APIResult add(@RequestBody @Validated UpdFenceDto dto) throws Exception {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "item", name = "详情围栏")
    @RequiresPermissions(API + "item")
    @Menu("电子围栏")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping(value = "del", name = "围栏删除")
    @RequiresPermissions(API + "del")
    @Menu("电子围栏")
    public APIResult del(@RequestParam String ids) throws Exception {
        service.del(ids);
        return APIResult.success();
    }


    @GetMapping(value = "list", name = "围栏列表")
    @RequiresPermissions(API + "list")
    @Menu("电子围栏")
    public APIResult list(@Validated PageFenceDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }


    @GetMapping(value = "deviceItem", name = "围栏详情")
    @RequiresPermissions(API + "deviceItem")
    @Menu("电子围栏")
    public APIResult deviceItem(@RequestParam String deviceId) throws ByteException {
        return APIResult.success().setValue(service.fenceItem(deviceId));
    }

    @GetMapping(value = "typeList", name = "禁止类型列表")
    @RequiresPermissions(API + "typeList")
    @Menu("电子围栏")
    public Object faceReg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CloneNotSupportedException {
        return service.typeList();
    }

}