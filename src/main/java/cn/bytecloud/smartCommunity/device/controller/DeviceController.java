package cn.bytecloud.smartCommunity.device.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.device.dto.AddDeviceDto;
import cn.bytecloud.smartCommunity.device.dto.DevicePageDto;
import cn.bytecloud.smartCommunity.device.dto.PageAppListDto;
import cn.bytecloud.smartCommunity.device.dto.UpdDeviceDto;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.station.dto.AddStationDto;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.dto.UpdStationDto;
import cn.bytecloud.smartCommunity.util.Base64Utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static cn.bytecloud.smartCommunity.device.controller.DeviceController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class DeviceController {
    public static final String API = "/api/device/";

    @Autowired
    private DeviceService service;

    @PostMapping(value = "add", name = "添加")
    @RequiresPermissions(API+"add")
    @Menu("摄像机管理")
    public APIResult add(@RequestBody @Validated AddDeviceDto dto) throws ByteException {
        return APIResult.success().setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions(API+"item")
    @Menu("摄像机管理")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions(API+"upd")
    @Menu("摄像机管理")
    public APIResult upd(@RequestBody @Validated UpdDeviceDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions(API+"del")
    @Menu("摄像机管理")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "筛选/搜索")
    @RequiresPermissions(API+"list")
    @Menu("摄像机管理")
    public APIResult list(@Validated DevicePageDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping(value = "rpc/list")
    public APIResult rpcList(@Validated BasePageDto dto) throws ByteException {
        return APIResult.success().setValue(service.rpcList(dto));
    }

    @GetMapping(value = "map/list")
    public APIResult mapList() throws ByteException {
        return APIResult.success().setValue(service.mapList());
    }

    @GetMapping(value = "app/list", name = "app列表")
    @RequiresPermissions(API+"app/list")
    @Menu("摄像机管理")
    public APIResult list(@Validated PageAppListDto dto) throws ByteException {
        return APIResult.success().setValue(service.appList(dto));
    }
}
