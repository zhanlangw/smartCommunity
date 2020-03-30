package cn.bytecloud.smartCommunity.video.realpaly.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public interface PushHandler {

    ConcurrentMap<String, Object> push(Map<String, Object> paramMap) throws IOException;

    String getComm4Map(Map<String, Object> paramMap);

    String getCapString(Map<String,Object> map) throws IOException;


}
