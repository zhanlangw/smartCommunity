package cn.bytecloud.smartCommunity.userAddress.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.userAddress.dto.UserAddressPageDto;
import cn.bytecloud.smartCommunity.userAddress.service.UserAddressService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.bytecloud.smartCommunity.userAddress.controller.UserAddressController.API;

@RestController
@RequestMapping(API)
public class UserAddressController {
    public static final String API = "/api/useraddress/";

    @Autowired
    private UserAddressService service;

    /**
     * 金纬度上传
     *
     * @return
     */
    @GetMapping("add")
    public APIResult add(@RequestParam Double longitude, @RequestParam Double latitude) {
        service.add(longitude, latitude);
        return APIResult.success();
    }

    @GetMapping(value = "list", name = "人员轨迹")
    public APIResult list(@Validated UserAddressPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping(value = "supervision", name = "工作人员")
    public APIResult supervision() {
        return APIResult.success().setValue(service.supervision());
    }
}
