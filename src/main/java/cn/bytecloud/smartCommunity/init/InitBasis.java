package cn.bytecloud.smartCommunity.init;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.blacklist.dao.BlacklistRepository;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.dao.DeviceRepository;
import cn.bytecloud.smartCommunity.device.dto.UpdDeviceDto;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.video.login.service.LoginService;
import cn.bytecloud.smartCommunity.work.DeviceDataHandler;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.List;

@Component
public class InitBasis {

    @Autowired
    private BasisService service;

    @Autowired
    private DeviceDataHandler deviceDataHandler;

    @Autowired
    private LoginService loginService;

    @Autowired
    private DeviceService  deviceService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private DeviceRepository repository;

    @PostConstruct
    public void initBasis(){
        if (null == service.findFirst()) {
            Basis basis = new Basis();
            basis.setId(UUIDUtil.getUUID());
            basis.setName("机投桥街道办");
            basis.setFileMaxSize(10L);//10 mb
            basis.setTimeOut(24L);//24h
            basis.setLongitude("103.994038");
            basis.setLatitude("30.651724");
            service.save(basis);
        }
    }

    @PostConstruct
    public void initVideo(){
        final long timeInterval = 1000*5;
        Runnable runnable = () -> {
            String url = null;
            String ip = null;
            Integer port = null;
            String username = null;
            String password = null;
            String resultString = null;
            JSONObject result = null;
            JSONObject value = null;
            List<Device> all = deviceService.findAll();
            while (true) {
                if (all!=null){
                    for (Device device : all) {
                        try {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                            url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.updDeviceStatus;
                            device =  deviceService.findById(device.getId());
                            ip = device.getIp();
                            port = device.getTcpPort();
                            username = device.getUsername();
                            password = device.getPassword();
                            url += "?ip=" + ip + "&port=" + port + "&username=" + username + "&password=" +password;
                            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
                            resultString= restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
                            result = JSONObject.parseObject(resultString);
                            value = JSONObject.parseObject(result.getString("value"));
                            if (value.getIntValue("linkStatus") == 0){
                                device.setStatus(DeviceStatus.ONLINE);
                                repository.save(device);
                            }else if (value.getIntValue("linkStatus") == 1){
                                device.setStatus(DeviceStatus.LESS);
                                repository.save(device);
                            }
                            Thread.sleep(timeInterval);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @PostConstruct
    public void initHandler(){
        deviceDataHandler.execute();
    }
}
