package cn.bytecloud.smartCommunity.video.realpaly.dao;

import java.util.concurrent.ConcurrentMap;


public interface HandlerDao {


    ConcurrentMap<String,Object> get(String pushId);

    void set(String key, ConcurrentMap<String, Object> resultMap);

    ConcurrentMap getAll();

    void delete(String pushId);

    boolean isHave(String pushId);

}
