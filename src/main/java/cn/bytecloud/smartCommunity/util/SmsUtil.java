package cn.bytecloud.smartCommunity.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SmsUtil {
    public static void sendSms(String telephone, String content) {
        log.info("开始发送短信,," + telephone + "-------" + content);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        String url = "http://106.15.238.82:19524/sms-partner/access/b07898/sendsms";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientid", "b07898");
        jsonObject.put("password", "e9bc0e13a8a16cbb07b175d92a113126");
        jsonObject.put("mobile", telephone);
        jsonObject.put("smstype", "0");
        jsonObject.put("content", "【安徽易木】" + content);
        HttpEntity<String> data = new HttpEntity<>(jsonObject.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(url, HttpMethod.POST, data, String.class);
        log.info(respose.toString());
    }
}
