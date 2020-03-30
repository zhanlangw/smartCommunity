package cn.bytecloud.smartCommunity.device.service;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.dao.DeviceDao;
import cn.bytecloud.smartCommunity.device.dao.DeviceRepository;
import cn.bytecloud.smartCommunity.device.dto.*;
import cn.bytecloud.smartCommunity.device.entity.DevicFeatuers;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.entity.DeviceStatus;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.smartCommunity.device.entity.DevicFeatuers.INTELLIGENT;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao dao;
    @Autowired
    private DeviceRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SystemConstant systemConstant;

    @Override
    public DeviceItemDto add(AddDeviceDto dto) throws ByteException {
        if (repository.findFirstByNum(dto.getNum()) != null) {
            throw new ByteException("编号已经存在");
        }
        Device device = dto.toData();
        device.setId(UUIDUtil.getUUID());
        device.setCreateTime(System.currentTimeMillis());
        device.setCreatorId(UserUtil.getUserId());
        rpcUpdDevice(device);
        repository.save(device);
//        ThreadPool.threadPool.execute(() -> {

//        });
        return EntityUtil.entityToDto(device, DeviceItemDto.class);
    }

    private void rpcDelDevice(String id) throws ByteException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.delDevice;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devId", id);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("删除人脸数据:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }

    }


    private void rpcUpdDevice(Device device) throws ByteException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.updDevice;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devId", device.getId());
        jsonObject.put("devName", device.getName());
        jsonObject.put("ip", device.getIp());
        jsonObject.put("port", device.getRtspPort());
        jsonObject.put("username", device.getUsername());
        jsonObject.put("password", device.getPassword());
        jsonObject.put("rtspUrl", device.getRtspAddress());
        jsonObject.put("enableAI", device.getFeatures() == DevicFeatuers.INTELLIGENT);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.debug("更新摄像机数据数据 ->  摄像机id:" + device.getId() + "  ->  " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
    }

    @Override
    public DeviceItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), DeviceItemDto.class);
    }

    @Override
    public DeviceItemDto upd(UpdDeviceDto dto) throws ByteException {
        Device device = repository.findFirstByNum(dto.getNum());
        if (device != null && !device.getId().equals(dto.getId())) {
            throw new ByteException("编号已经存在");
        }

        Device data = dto.toData();
//        ThreadPool.threadPool.execute(() -> {
        rpcUpdDevice(data);
//        });
        dao.save(data);
        return EntityUtil.entityToDto(data, DeviceItemDto.class);
    }

    @Override
    public void del(String ids) throws ByteException {
        for (String id : ids.split(",")) {
            rpcDelDevice(id);
            repository.delete(id);
        }
    }

    @Override
    public PageModel list(DevicePageDto dto) {
        return dao.list(dto);
    }

    @Override
    public List<Device> findByUnitId(String id) {
        return repository.findByUnitId(id);
    }

    @Override
    public Object rpcList(BasePageDto dto) {
        PageModel pageModel = dao.rpcList(dto);
        pageModel.setValue(EntityUtil.entityListToDtoList(pageModel.getValue(), RpcDto.class));
        return pageModel;
    }

    @Override
    public Device findById(String deviceId) {
        return repository.findOne(deviceId);
    }

    @Override
    public Object mapList() {
        List<Map<String, String>> data = new ArrayList<>();
        dao.mapList().forEach(device -> {
            Map<String, String> map = new HashedMap<>();
            map.put("latitude", device.getLatitude());
            map.put("longitude", device.getLongitude());
            map.put("name", device.getName());
            map.put("id", device.getId());
            data.add(map);
        });
        return data;
    }

    @Override
    public Object appList(PageAppListDto dto) {
        User user = UserUtil.getUser();
        Set<String> deviceIds = roleService.findByIds(user.getRoleIds()).stream().map(Role::getDeviceIds).flatMap(Collection::stream).collect(Collectors.toSet());
        return dao.appList(dto, deviceIds);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByOnline() {
        return dao.countByOnline(DeviceStatus.ONLINE);
    }

    @Override
    public List<Device> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Device> findByName(String title) {
       return dao.findByName(title);
    }
}
