package cn.bytecloud.smartCommunity.video.login.controller;

import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static cn.bytecloud.smartCommunity.video.login.controller.LoginController.API;


@RestController
@RequestMapping(API)
public class LoginController {

    public static final String API = "/api/video/";

    @Autowired
    private LoginService loginService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemConstant systemConstant;

    @GetMapping(value = "login",name = "登录摄像头")
    public APIResult login_video(@RequestParam String id) throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoLogin;
        url +="?id=" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }

    @GetMapping(value = "logout",name = "退出登录")
    public APIResult loginOut() throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoLogout;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }

}
