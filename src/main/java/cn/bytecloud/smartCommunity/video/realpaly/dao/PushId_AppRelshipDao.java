package cn.bytecloud.smartCommunity.video.realpaly.dao;

import java.util.List;

public interface PushId_AppRelshipDao {

    String getPushId(String appName);

    void set(String appName, String pushId);

    void delete(String appName);

    List<String> getAll();

    boolean isHave(String appName);

}
