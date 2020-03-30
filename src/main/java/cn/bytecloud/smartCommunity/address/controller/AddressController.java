package cn.bytecloud.smartCommunity.address.controller;

import cn.bytecloud.smartCommunity.address.service.AddressService;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping(API)
public class AddressController {

    public static final String API = "/api/address/";
    @Autowired
    private AddressService service;

    @RequestMapping(value = "/not_login")
    public APIResult notLogin() throws IOException {
        return APIResult.failure(ErrorCode.PARAMETER_NOT_LOGIN).setMessage("未登录");
    }

    @PostMapping("add")
    public APIResult add(@RequestBody MultipartFile file) throws IOException {
        service.add(file);
        return APIResult.success();
    }

    @GetMapping("data")
    public APIResult data() {
        return APIResult.success().setValue(service.getData());
    }

}
