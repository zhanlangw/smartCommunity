package cn.bytecloud.smartCommunity.video.realpaly.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.device.dto.DeviceItemDto;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.realpaly.dto.RealPlayDto;
import cn.bytecloud.smartCommunity.video.realpaly.service.PushManager;
import cn.bytecloud.smartCommunity.video.realpaly.service.RealPlayService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.smartCommunity.video.realpaly.controller.RealPlayController.API;

@RestController
@RequestMapping(API)
@Menu("视频监控")
public class RealPlayController {

    public static final String API = "/api/video/";

    @Autowired
    private RealPlayService realPlayService;

    @GetMapping(value = "play",name = "开始监控")
    @RequiresPermissions((API+"play"))
    public APIResult start(@RequestParam String id, @RequestParam String type, HttpServletRequest request) throws ByteException{
        return APIResult.success().setValue(realPlayService.start(id, type, request));
    }

    @GetMapping(value = "playClose",name = "关闭监控")
    public APIResult closePlay(@RequestParam String id, @RequestParam String type, HttpServletRequest request) throws ByteException{
        realPlayService.close(id, type, request);
        return APIResult.success("关闭监控");
    }

    @GetMapping(value = "play_close",name = "关闭监控")
    public APIResult oldClosePlay(@RequestParam String id, @RequestParam String type, HttpServletRequest request) throws ByteException{
        realPlayService.close(id, type, request);
        return APIResult.success("关闭监控");
    }

    @GetMapping(value = "/ai/play", name = "智能监控")
    public String aiPlay(@RequestParam String id) throws ByteException {
        return realPlayService.aiPlay(id);
    }

    @GetMapping(value = "/ai/stop", name = "关闭智能监控")
    public String aiStop(@RequestParam String id) throws ByteException {
        return realPlayService.aiStop(id);
    }


}
