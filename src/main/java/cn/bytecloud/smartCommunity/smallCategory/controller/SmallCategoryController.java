package cn.bytecloud.smartCommunity.smallCategory.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.smallCategory.dto.AddSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.CategoryPageDto;
import cn.bytecloud.smartCommunity.smallCategory.dto.UpdSmallCategoryDto;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.smallCategory.controller.SmallCategoryController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class SmallCategoryController {
    public static final String API = "/api/smallcategory/";

    @Autowired
    private SmallCategoryService service;

    @PostMapping(value = "add", name = "添加")
    @RequiresPermissions(API+"add")
    @Menu("小类管理")
    public APIResult add(@RequestBody @Validated AddSmallCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.add(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions(API+"item")
    @Menu("小类管理")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions(API+"upd")
    @Menu("小类管理")
    public APIResult upd(@RequestBody @Validated UpdSmallCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions(API+"del")
    @Menu("小类管理")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "搜索/筛选")
    @RequiresPermissions(API+"list")
    @Menu("小类管理")
    public APIResult list(@Validated CategoryPageDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }
}
