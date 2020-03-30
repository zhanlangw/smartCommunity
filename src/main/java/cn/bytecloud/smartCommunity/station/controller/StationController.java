package cn.bytecloud.smartCommunity.station.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dto.AddSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.UpdSmallCategoryDto;
import cn.bytecloud.smartCommunity.station.dto.AddStationDto;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.dto.UpdStationDto;
import cn.bytecloud.smartCommunity.station.service.StationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static cn.bytecloud.smartCommunity.station.controller.StationController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class StationController {
    public static final String API = "/api/station/";

    @Autowired
    private StationService service;

    @PostMapping(value = "add", name = "添加")
    @RequiresPermissions(API+"add")
    @Menu("站点管理")
    public APIResult add(@RequestBody @Validated AddStationDto dto) throws ByteException {
        return APIResult.success().setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions(API+"item")
    @Menu("站点管理")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions(API+"upd")
    @Menu("站点管理")
    public APIResult upd(@RequestBody @Validated UpdStationDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions(API+"del")
    @Menu("站点管理")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "列表")
    @RequiresPermissions(API+"list")
    @Menu("站点管理")
    public APIResult list(@Validated StationPageDto dto, HttpServletRequest request) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping(value = "maplist", name = "地图列表")
    public APIResult mapList() throws ByteException {
        return APIResult.success().setValue(service.mapList());
    }
}
