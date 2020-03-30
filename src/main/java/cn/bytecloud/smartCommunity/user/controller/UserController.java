package cn.bytecloud.smartCommunity.user.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.user.dto.*;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.UserUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static cn.bytecloud.smartCommunity.user.controller.UserController.API;


@RestController
@RequestMapping(API)
@Menu("高级管理")
public class UserController {

    public static final String API = "/api/user/";

    @Autowired
    private UserService service;

    @PostMapping(value = "add", name = "新建")
    @RequiresPermissions((API + "add"))
    @Menu("用户管理")
    public APIResult add(@RequestBody @Validated AddUserInUnitDto dto) throws ByteException {
        return APIResult.success("创建成功").setValue(service.add(dto));
    }

    @PostMapping(value = "updInUnit", name = "组织机构中修改")
    @RequiresPermissions((API + "updInUnit"))
    @Menu("用户管理")
    public APIResult updInUnit(@RequestBody @Validated UpdUserInUnitDto dto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.updInUnit(dto));
    }

    @PostMapping(value = "upd", name = "修改")
    @RequiresPermissions((API + "upd"))
    @Menu("用户管理")
    public APIResult upd(@RequestBody @Validated UpdUserDto dto) throws ByteException {
        return APIResult.success("修改成功").setValue(service.upd(dto));
    }

    @GetMapping(value = "itemInUnit", name = "组织机构中详情")
    @RequiresPermissions((API + "itemInUnit"))
    @Menu("用户管理")
    public APIResult itemInUnit(@RequestParam String id) throws ByteException {
        return APIResult.success("查询成功").setValue(service.itemInUnit(id));
    }

    @GetMapping(value = "list", name = "列表")
    @RequiresPermissions((API + "list"))
    @Menu("用户管理")
    public APIResult list(@Validated PageUserDto dto) throws ByteException {
        return APIResult.success("查询成功").setValue(service.list(dto));
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    @Menu("用户管理")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success("查询成功").setValue(service.item(id));
    }

    @GetMapping(value = "del", name = "删除")
    @RequiresPermissions((API + "del"))
    @Menu("用户管理")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success("删除成功");
    }

    @PostMapping(value = "login", name = "登录")
    public APIResult login(@RequestBody @Validated LoginDto dto, HttpServletRequest req) throws ByteException {
        return APIResult.success().setValue(service.login(dto));
    }

    @PostMapping(value = "password/reset", name = "重置密码")
    @RequiresPermissions({API + "password/reset"})
    @Menu("用户管理")//超级管理员可以重置别人的密码
    public APIResult resetPasswrod(@RequestBody @Validated ResetPasswordDto dto) throws ByteException {
        service.resetPasswrod(dto);
        return APIResult.success("重置成功");
    }

    @GetMapping(value = "image/upd", name = "修改头像")
    @RequiresPermissions({API + "image/upd"})
    @Menu("用户管理")
    public APIResult updImage(@RequestParam String image) {
        service.updImage(UserUtil.getUserId(), image);
        return APIResult.success();
    }

    @GetMapping(value = "import", name = "导入")
    @RequiresPermissions((API + "import"))
    @Menu("用户管理")
    public APIResult importUsers(@RequestParam String path) throws ByteException, IOException {
        service.importUsers(path);
        return APIResult.success();

    }

    @PostMapping(value = "upd/password", name = "修改密码")
    @RequiresPermissions((API + "upd/password"))
    @Menu("用户管理")
    public APIResult updPassword(@Validated @RequestBody UpdPasswordDto dto) throws ByteException {
        service.updpassword(dto);
        SecurityUtils.getSubject().logout();
        return APIResult.success();
    }

    @GetMapping("open/sound")
    public APIResult openAlarmSound() {
        service.openAlarmSound();
        return APIResult.success();
    }

    @GetMapping("close/sound")
    public APIResult closeAlarmSound() {
        service.closeAlarmSound();
        return APIResult.success();
    }

    @GetMapping("sound")
    public APIResult sound() {
        return APIResult.success().setValue(service.sound());
    }


    @GetMapping(value ="getout", name = "注销")
    public APIResult logout() throws ByteException {
        service.logout();
        return APIResult.success().setValue("注销成功");
    }


}




