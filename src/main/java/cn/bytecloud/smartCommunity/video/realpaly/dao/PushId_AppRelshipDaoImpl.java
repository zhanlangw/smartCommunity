package cn.bytecloud.smartCommunity.video.realpaly.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushId_AppRelshipDaoImpl implements PushId_AppRelshipDao {

    private Map<String,String> map = new HashMap<>();

    @Override
    public String getPushId(String appName) {
        return this.map.get(appName);
    }

    @Override
    public void set(String appName, String pushId) {
        this.map.put(appName, pushId);
    }

    @Override
    public void delete(String appName) {
        this.map.remove(appName);
    }

    @Override
    public List<String> getAll() {
        return null;
    }

    @Override
    public boolean isHave(String appName) {
        return this.map.containsKey(appName);
    }
}
