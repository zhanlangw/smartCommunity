package cn.bytecloud.smartCommunity.blacklist.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.blacklist.dto.AddBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistPageDto;
import cn.bytecloud.smartCommunity.blacklist.dto.UpdBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.service.BlacklistService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.station.dto.AddStationDto;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.dto.UpdStationDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.blacklist.controller.BlacklistController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class BlacklistController {
    public static final String API = "/api/blacklist/";

    @Autowired
    private BlacklistService service;

    @PostMapping(value = "add", name = "添加")
    @RequiresPermissions(API+"add")
    @Menu("黑名单管理")
    public APIResult add(@RequestBody @Validated AddBlacklistDto dto) throws Exception {
        return APIResult.success().setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions(API+"item")
    @Menu("黑名单管理")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions(API+"upd")
    @Menu("黑名单管理")
    public APIResult upd(@RequestBody @Validated UpdBlacklistDto dto) throws Exception {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions(API+"del")
    @Menu("黑名单管理")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "筛选/搜索")
    @RequiresPermissions(API+"list")
    @Menu("黑名单管理")
//    @Cacheable(value = "prodetsd",key = "sdafqwvasdfjwevse")
    public APIResult list(@Validated BlacklistPageDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }
}
