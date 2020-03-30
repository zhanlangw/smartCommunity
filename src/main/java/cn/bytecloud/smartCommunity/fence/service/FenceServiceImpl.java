package cn.bytecloud.smartCommunity.fence.service;

import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.fence.dao.FenceDao;
import cn.bytecloud.smartCommunity.fence.dao.FenceRepository;
import cn.bytecloud.smartCommunity.fence.dto.AddFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.FenceItemDto;
import cn.bytecloud.smartCommunity.fence.dto.PageFenceDto;
import cn.bytecloud.smartCommunity.fence.dto.UpdFenceDto;
import cn.bytecloud.smartCommunity.fence.entity.Fence;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FenceServiceImpl implements FenceService {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private FenceRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private FenceDao dao;

    private void rpcUpdFace(Fence fence) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.updFence;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devId", fence.getDeviceId());
        jsonObject.put("fenceId", fence.getId());
        jsonObject.put("name", fence.getName());
        jsonObject.put("cells", fence.getNum());
        jsonObject.put("rejects", fence.getType());
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("更新人脸数据:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            repository.delete(fence.getId());
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
    }

    private void delFence(JSONArray jsonArray) throws Exception {
        System.out.println(jsonArray.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.delFence;
        HttpEntity<String> data = new HttpEntity<>(jsonArray.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("更新人脸数据:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
    }

    @Override
    public FenceItemDto add(AddFenceDto dto) throws Exception {
        if (repository.findByNameAndDeviceId(dto.getName(), dto.getDeviceId()).size() > 0) {
            throw new ByteException("名称重复");
        }

        Fence fence = dao.save(dto.toData());
        rpcUpdFace(fence);
        return EntityUtil.entityToDto(fence, FenceItemDto.class);
    }

    @Override
    public FenceItemDto upd(UpdFenceDto dto) throws Exception {
        if (repository.findByNameAndDeviceId(dto.getName(), dto.getDeviceId()).stream().anyMatch(fence -> !fence.getId().equals(dto.getId()))) {
            throw new ByteException("名称重复");
        }
        Fence fence = dao.save(dto.toData());
        rpcUpdFace(fence);
        return EntityUtil.entityToDto(fence, FenceItemDto.class);
    }

    @Override
    public FenceItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), FenceItemDto.class);
    }

    @Override
    public void del(String ids) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (String id : ids.split(",")) {
            Map map = new HashMap();
            Fence fence = repository.findOne(id);
            map.put("devId", fence.getDeviceId());
            map.put("fenceId", fence.getId());
            jsonArray.add(map);
        }
        delFence(jsonArray);
        dao.del(ids);
    }

    @Override
    public Object list(PageFenceDto dto) {
        Set<String> deviceIds = null;
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            deviceIds = deviceService.findByName(dto.getTitle()).stream().map(Device::getId).collect(Collectors.toSet());

        }
        return dao.list(dto, deviceIds);
    }

    @Override
    public Object fenceItem(String deviceId) {
        return EntityUtil.entityListToDtoList(repository.findByDeviceId(deviceId), FenceItemDto.class);
    }

    @Override
    public Object typeList() {
        HttpHeaders headers = new HttpHeaders();
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.fenceOpt;
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        log.info("更新人脸数据:   " + respose.toString());
        return JSONObject.parseObject(respose.getBody());
    }
}
