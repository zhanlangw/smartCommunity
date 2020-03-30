package cn.bytecloud.smartCommunity.bigCategory.controller;

import cn.bytecloud.smartCommunity.bigCategory.dto.AddBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.dto.UpdBigCategoryDto;
import cn.bytecloud.smartCommunity.bigCategory.service.BigCategoryService;
import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.bigCategory.controller.BigCategoryController.API;

@RestController
@RequestMapping(API)
@Menu("高级管理")
public class BigCategoryController {
    public static final String API = "/api/bigcategory/";

    @Autowired
    private BigCategoryService service;

    @PostMapping(value = "add",name = "添加")
    @RequiresPermissions(API+"add")
    @Menu("大类管理")
    public APIResult add(@RequestBody @Validated AddBigCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.add(dto));
    }

    @PostMapping(value = "upd",name = "修改")
    @RequiresPermissions(API+"add")
    @Menu("大类管理")
    public APIResult upd(@RequestBody @Validated UpdBigCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @GetMapping(value = "item",name = "详情")
    @RequiresPermissions(API+"item")
    @Menu("大类管理")
    public APIResult item(@RequestParam String id){
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping(value = "del",name = "删除")
    @RequiresPermissions(API+"del")
    @Menu("大类管理")
    public APIResult del(@RequestParam String ids) throws ByteException {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "list",name = "筛选/搜索")
    @RequiresPermissions(API+"list")
    @Menu("大类管理")
    public APIResult list(){
        return APIResult.success().setValue(service.list());
    }
}
