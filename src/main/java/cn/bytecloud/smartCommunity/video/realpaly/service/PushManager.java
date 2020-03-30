package cn.bytecloud.smartCommunity.video.realpaly.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PushManager {

    String push(Map<String, Object> map);

    void closePush(String appName);

    List<String> viewAppName();

    void myPush(Map<String, Object> map) throws IOException;


}
