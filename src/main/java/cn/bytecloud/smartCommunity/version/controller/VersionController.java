package cn.bytecloud.smartCommunity.version.controller;

import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.version.entity.VersionType;
import cn.bytecloud.smartCommunity.version.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("version")
public class VersionController {

    @Autowired
    private VersionService service;

    @GetMapping("info")
    public APIResult getVersion(@RequestParam VersionType type){
        return APIResult.success().setValue(service.getInfo(type));
    }
}
