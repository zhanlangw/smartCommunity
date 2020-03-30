package cn.bytecloud.smartCommunity.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class PushMsg {
    private static final String PUSHER_URL = "https://api.jpush.cn/v3/push";
    private static final String APP_KEY = "abd7ece4274dca09c7f1cf61";
    private static final String MATER_SECRET = "4942f1c9f5715ca8b5ea5495";

    public static void pushMsg(String msg, List<String> alias, String badge){
        //推送的body
        JSONObject body = new JSONObject();
        //推送所有平台,
        body.put("platform", "all");

        //推送目标
        JSONObject audience = new JSONObject();
        audience.put("alias", alias);

        body.put("audience", audience);

        //推送内容
        JSONObject notification = new JSONObject();
        notification.put("alert", msg);

        //自定义扩展字段
        JSONObject extras = new JSONObject();
        extras.put("badge", badge);

        JSONObject android = new JSONObject();
        android.put("extras", extras);
        notification.put("android", android);


        JSONObject ios = new JSONObject();
        ios.put("extras", extras);
        ios.put("sound", "default");
        notification.put("ios", ios);

        body.put("notification", notification);


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", "Basic " + new BASE64Encoder().encode((APP_KEY+":"+MATER_SECRET).getBytes()));

        HttpEntity<String> data = new HttpEntity<>(body.toJSONString(), headers);
        ResponseEntity<String> respose = restTemplate.exchange(PUSHER_URL, HttpMethod.POST, data, String.class);
        log.info("app 消息推送:::"+respose.toString());
    }

    public static void main(String[] args) {
        List<String> users = new ArrayList<>();
        users.add("e8ed53a1925e427582cd347d4b9a61fe");
        users.add("user2");
        pushMsg("测试", users, null);
    }
}
