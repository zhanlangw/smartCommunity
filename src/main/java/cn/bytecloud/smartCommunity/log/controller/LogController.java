package cn.bytecloud.smartCommunity.log.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.log.dto.LogPageDto;
import cn.bytecloud.smartCommunity.log.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.bytecloud.smartCommunity.log.controller.LogController.API;

@RestController
@RequestMapping(API)
public class LogController {
    public static final String API = "/api/log/";

    @Autowired
    private LogService service;

    @GetMapping(value = "list",name = "办理流程")
    @RequiresPermissions(("/api/work/item"))
    public APIResult list(@RequestParam String id, @RequestParam Integer type) throws ByteException {
        return APIResult.success().setValue(service.list(id,type));
    }
}
