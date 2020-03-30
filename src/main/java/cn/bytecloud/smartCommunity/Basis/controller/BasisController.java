package cn.bytecloud.smartCommunity.Basis.controller;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.bytecloud.smartCommunity.Basis.controller.BasisController.API;

@RequestMapping(API)
@RestController
@Menu("高级管理")
public class BasisController {
    public static final String API = "/api/basis/";

    @Autowired
    private BasisService service;

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions(API + "upd")
    @Menu("基础配置")
    public APIResult upd(@RequestBody @Validated Basis basis) {
        return APIResult.success().setMessage("修改成功！").setValue(service.save(basis));
    }

    @GetMapping(value = "item", name = "详情")
//    @RequiresPermissions(API + "item")
//    @Menu("基础配置")
    public APIResult item() {
        return APIResult.success().setValue(service.item());
    }
}
