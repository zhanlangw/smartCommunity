package cn.bytecloud.smartCommunity.video.capturePicture.service;

import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.dto.DeviceItemDto;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.FileUtil;
import cn.bytecloud.smartCommunity.util.PathUtil;
import cn.bytecloud.smartCommunity.util.ProUtil;
import cn.bytecloud.smartCommunity.video.realpaly.service.PushManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CaptureServiceImpl implements CaptureService{

    @Autowired
    private PushManager pushManager;

    @Autowired
    private DeviceService service;

    @Autowired
    private SystemConstant systemConstant;

    @Override
    public String capture(String id) throws ByteException {

        Calendar now = Calendar.getInstance();
        DeviceItemDto itemDto = service.item(id);
        String input = systemConstant.outPut;
//        String[] split = itemDto.getIp().split("//");
        Long fileId = now.getTimeInMillis();
        String ip = itemDto.getIp();
        String[] strings = ip.split("\\.");
        String fname = strings[strings.length-1]+1;
        Map<String, Object> map = new HashMap<>(16);
        map.put("fname", fname);
        map.put("appName", itemDto.getId());
        map.put("input", input);
        map.put("fileId", fileId);
//        pushManager.push(map);
        try {
            pushManager.myPush(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName;
        StringBuilder str = new StringBuilder();
        str.append("/home/bytecloud/smart_community/fileData");
        str.append("/capture/").append(now.get(Calendar.YEAR)).append(now.get(Calendar.MONTH) + 1);
        str.append(now.get(Calendar.DAY_OF_MONTH));
        str.append("/").append(map.get("fileId")).append(".jpg");
        fileName = str.toString();
        return fileName;
    }
}
