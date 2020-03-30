package cn.bytecloud.smartCommunity.video.realpaly.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;


public class HandlerDaoImpl implements HandlerDao {

    private Map<String,ConcurrentMap<String,Object>> map = new HashMap<>();

    @Override
    public ConcurrentMap<String,Object> get(String pushId) {
        return this.map.get(pushId);
    }

    @Override
    public void set(String key, ConcurrentMap<String, Object> resultMap) {
        this.map.put(key, resultMap);
    }

    @Override
    public ConcurrentMap getAll() {
        return null;
    }

    @Override
    public void delete(String pushId) {
        this.map.remove(pushId);
    }

    @Override
    public boolean isHave(String pushId) {
        return this.map.containsKey(pushId);
    }
}
