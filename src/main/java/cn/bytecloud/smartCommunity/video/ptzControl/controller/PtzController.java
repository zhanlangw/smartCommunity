package cn.bytecloud.smartCommunity.video.ptzControl.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.ptzControl.service.PtzControlService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static cn.bytecloud.smartCommunity.video.ptzControl.controller.PtzController.API;

@RestController
@RequestMapping(API)
public class PtzController {

    public static final String API = "/api/video/";

    @Autowired
    private PtzControlService ptzControlService;

    @Autowired
    private DeviceService service;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemConstant systemConstant;

    @GetMapping(value = "controlUp",name = "上")
    public APIResult up_control(@RequestParam String id) throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoUp;
        url +="?id=" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }

    @GetMapping(value = "controlDown",name = "下")
    public APIResult down_control(@RequestParam String id) throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoDown;
        url +="?id=" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }

    @GetMapping(value = "controlLeft",name = "左")
    public APIResult left_control(@RequestParam String id) throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoLeft;
        url +="?id=" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }

    @GetMapping(value = "controlRight",name = "右")
    public APIResult right_control(@RequestParam String id) throws ByteException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String url = "http://" + systemConstant.videoIp + ":" + systemConstant.videoPort + systemConstant.videoRight;
        url +="?id=" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        return APIResult.success().setValue(result);
    }


}
