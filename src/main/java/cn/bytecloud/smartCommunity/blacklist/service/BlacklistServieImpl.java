package cn.bytecloud.smartCommunity.blacklist.service;

import cn.bytecloud.smartCommunity.blacklist.dao.BlacklistDao;
import cn.bytecloud.smartCommunity.blacklist.dao.BlacklistRepository;
import cn.bytecloud.smartCommunity.blacklist.dto.AddBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistItemDto;
import cn.bytecloud.smartCommunity.blacklist.dto.BlacklistPageDto;
import cn.bytecloud.smartCommunity.blacklist.dto.UpdBlacklistDto;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.Base64Utils;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlacklistServieImpl implements BlacklistService {
    @Autowired
    private BlacklistDao dao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlacklistRepository repository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SystemConstant systemConstant;

    @Override
    public BlacklistItemDto add(AddBlacklistDto dto) throws Exception {
        Blacklist blacklist = dto.toData();
        blacklist.setId(UUIDUtil.getUUID());
        rpcUpdFace(blacklist);
        blacklist = dao.save(blacklist, true);

        return EntityUtil.entityToDto(blacklist, BlacklistItemDto.class);
    }

    private void rpcUpdFace(Blacklist blacklist) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.updFace;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refId", blacklist.getId());
        jsonObject.put("name", blacklist.getName());
        List<String> list = new ArrayList<>();
        list.add(Base64Utils.ImageToBase64ByLocal(blacklist.getImagePath()));
        if (EmptyUtil.isNotEmpty(blacklist.getLeftImagePath())) {
            list.add(Base64Utils.ImageToBase64ByLocal(blacklist.getLeftImagePath()));
        }
        if (EmptyUtil.isNotEmpty(blacklist.getRightImagePath())) {
            list.add(Base64Utils.ImageToBase64ByLocal(blacklist.getRightImagePath()));
        }
        jsonObject.put("images", list);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("更新人脸数据:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
    }


    private void rpcDelFace(String id) throws ByteException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://" + systemConstant.faceIp + ":" + systemConstant.facePort + systemConstant.delFace;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refId", id);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info("删除人脸数据:   " + respose.toString());
        JSONObject object = JSONObject.parseObject(respose.getBody());
        if (object.getIntValue("status") != 0) {
            throw new ByteException(object.getString("message"), object.getIntValue("status"));
        }
    }

    @Override
    public BlacklistItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), BlacklistItemDto.class);
    }

    @Override
    public BlacklistItemDto upd(UpdBlacklistDto dto) throws Exception {
        Blacklist blacklist = dto.toData();
        rpcUpdFace(blacklist);
        blacklist = dao.save(blacklist,false);
//        ThreadPool.threadPool.execute(() -> {

//        });
        return EntityUtil.entityToDto(blacklist, BlacklistItemDto.class);
    }

    @Override
    public void del(String ids) throws ByteException {
        List<String> models = Arrays.stream(SystemConstant.MODEL_IDS).collect(Collectors.toList());
        for (String id : ids.split(",")) {
            if (models.contains(id)) {
                throw new ByteException("物体识别禁止删除");
            }
            rpcDelFace(id);
            repository.delete(id);
        }

    }

    @Override
    public Object list(BlacklistPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public void init() {
        repository.findAll().forEach(blacklist -> {
            redisTemplate.opsForValue().set(blacklist.getId(), blacklist.getCode());
            redisTemplate.expire(blacklist.getId(), 10 * 365, TimeUnit.DAYS);
        });
    }

    @Override
    public Blacklist findById(String blacklistId) {
        return repository.findOne(blacklistId);
    }

    @Override
    public List<Blacklist> findByTypeId(String typeId) {
        return repository.findByTypeId(typeId);
    }
}
