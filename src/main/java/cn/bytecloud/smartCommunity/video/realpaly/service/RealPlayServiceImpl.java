package cn.bytecloud.smartCommunity.video.realpaly.service;

import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.dto.DeviceItemDto;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.LinuxProcessUtil;
import cn.bytecloud.smartCommunity.video.realpaly.dto.RealPlayDto;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RealPlayServiceImpl implements RealPlayService {

    @Autowired
    private PushManager pushManager;

    @Autowired
    private DeviceService service;

    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private RestTemplate restTemplate;

    private static Map<String, Integer> deviceMap = new ConcurrentHashMap<>();

    @Override
    public RealPlayDto start(String id, String type, HttpServletRequest request) throws ByteException{
        DeviceItemDto itemDto = service.item(id);
        if (itemDto.getStatus().equals(DeviceStatus.LESS)) {
            throw new ByteException("该摄像机不在线，暂时无法使用，请稍后重新开启监控！");
        }
        String input = itemDto.getIp();
        Integer port = itemDto.getTcpPort();
        String username = itemDto.getUsername();
        String password = itemDto.getPassword();
        String output = systemConstant.outPut;
        String outPort = systemConstant.outPort;
        String rtmpPort = systemConstant.rtmpPort;
        String user = systemConstant.user;
        String word = systemConstant.password;
        int subType = 1;
//        StringBuffer requestURL = request.getRequestURL();
//        if (requestURL.indexOf(systemConstant.inNet) != -1){
//            // 1为辅码
//            subType = 1;
//        }
        RemoteShellExecutor executor = new RemoteShellExecutor(output, user, word);

        Map<String, Object> map = new HashMap<>(16);
        map.put("port", port);
        map.put("input", input);
        map.put("output", output);
        map.put("username", username);
        map.put("password", password);
        map.put("rtmp", rtmpPort);
        map.put("user", user);
        map.put("word", word);
        map.put("subtype", subType);
        map.put("ffmpeg", systemConstant.ffmpeg);

        String[] strings = input.split("\\.");
        String appName = strings[strings.length - 1]+String.valueOf(subType);

        map.put("appName", appName);
        String subAddress = systemConstant.outAddress;
//        pushManager.push(map);
//        if (subType == 0){
//            subAddress = systemConstant.outPut;
//        }
//        if (requestURL.indexOf(systemConstant.inNet) != -1) {
//            subAddress = systemConstant.outPut;
//        }
        String address = "http://" + subAddress + ":" + outPort + "/hls/" + appName + ".m3u8";
        String newAppName = strings[strings.length - 1] + 0;
        String highAddress = "http://" + subAddress + ":" + outPort + "/hls/" + newAppName + ".m3u8";
        RealPlayDto dto = new RealPlayDto();
        Map unit = itemDto.getUnit();
        dto.setId(id);
        dto.setName(itemDto.getName());
        dto.setAddress(address);
        dto.setHighAddress(highAddress);
        dto.setUnitName((String) unit.get("name"));
        dto.setNum(itemDto.getNum());
        dto.setType(itemDto.getType().getEnumValue());
        dto.setFeatures(itemDto.getFeatures());

        if (deviceMap.containsKey(id) && deviceMap.get(id) >= 1) {
            try {
                if (executor.exec("ls /tmp/hls").contains(appName+".m3u8")){
                    deviceMap.put(id, deviceMap.get(id) + 1);
                    return dto;
                }else {
                    deviceMap.remove(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            pushManager.myPush(map);
            subType = 0;
            map.put("subtype", subType);
            map.put("appName", strings[strings.length - 1] + 0);
            pushManager.myPush(map);
            deviceMap.put(id, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int t = 500;
        int start = (int) Calendar.getInstance().getTimeInMillis();
        int end = (int) ((Calendar.getInstance().getTimeInMillis() - start));
        for (int i = 10000; i > end; end = end + t) {
            try {
                Thread.sleep(t);
                if (executor.exec("ls /tmp/hls").contains(appName+".m3u8")){
                    return dto;
                }
            } catch (Exception e) {
                deviceMap.remove(id);
                throw new ByteException("视频流无法输出到远程服务器！");
            }
        }
        deviceMap.remove(id);
        try {
            executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + appName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
            executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + newAppName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ByteException("请重新开启监控！");
    }

    @Override
    public void close(String id, String type, HttpServletRequest request) throws ByteException {
        DeviceItemDto itemDto = service.item(id);
        String input = itemDto.getIp();
        int subType = 1;
//        StringBuffer requestURI = request.getRequestURL();
//        if (requestURI.indexOf(systemConstant.inNet) != -1){
//            // 1为辅码
//            subType = 1;
//        }
        String[] strings = input.split("\\.");
        String appName = strings[strings.length - 1]+String.valueOf(subType);
        String newAppName = strings[strings.length - 1] + 0;
        RemoteShellExecutor executor = new RemoteShellExecutor(systemConstant.outPut, systemConstant.user, systemConstant.password);
        if (!deviceMap.containsKey(id) || deviceMap == null) {
            try {
                executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + appName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
                executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + newAppName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new ByteException("请开启视频监控！");
        }
        if (deviceMap.get(id) > 1) {
            deviceMap.put(id, deviceMap.get(id) - 1);
            return;
        } else {
            deviceMap.remove(id);
            try {
                executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + appName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
                executor.exec("kill -9 " + LinuxProcessUtil.getPID("rtmp://" + systemConstant.outPut + ":" + systemConstant.rtmpPort + "/hls/" + newAppName, systemConstant.outPut, systemConstant.user, systemConstant.password, systemConstant.split));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 定时关闭监控
     */
    @PostConstruct
    public void closeTimer() {
        RemoteShellExecutor executor = new RemoteShellExecutor(systemConstant.outPut, systemConstant.user, systemConstant.password);
        Runnable runnable = () -> {
            if (new Date().getHours() >= 21) {
                try {
                    executor.exec("pkill -9 ffmpeg");
                    deviceMap.clear();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public String aiPlay(String id) throws ByteException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.play;
        JSONObject jsonObject = new JSONObject();
        DeviceItemDto itemDto = service.item(id);
        if (itemDto == null) {
            throw new ByteException("该摄像机不存在！");
        }
        String name = itemDto.getName();
        String ip = itemDto.getIp();
        String port = itemDto.getRtspPort();
        String username = itemDto.getUsername();
        String password = itemDto.getPassword();
        String rtspUrl = itemDto.getRtspAddress();
        Integer manufacturer = Integer.valueOf(itemDto.getVendor());

        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("port", port);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("rtspUrl", rtspUrl);
        jsonObject.put("manufacturer", manufacturer);
        jsonObject.put("ip", ip);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("开启智能监控:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
        return respose.getBody();
    }

    @Override
    public String aiStop(String id) throws ByteException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.stop;
        JSONObject jsonObject = new JSONObject();
        DeviceItemDto itemDto = service.item(id);
        if (itemDto == null) {
            throw new ByteException("该摄像机不存在！");
        }
        String name = itemDto.getName();
        String ip = itemDto.getIp();
        String port = itemDto.getRtspPort();
        String username = itemDto.getUsername();
        String password = itemDto.getPassword();
        String rtspUrl = itemDto.getRtspAddress();
        Integer manufacturer = Integer.valueOf(itemDto.getVendor());
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("port", port);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("rtspUrl", rtspUrl);
        jsonObject.put("manufacturer", manufacturer);
        jsonObject.put("ip", ip);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("开启智能监控:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
        return respose.getBody();
    }
}
