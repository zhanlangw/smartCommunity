package cn.bytecloud.smartCommunity.video.realpaly.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.realpaly.dao.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Service
public class PushManagerImpl implements PushManager {

    private PushHandler pusher = new PushHandlerImpl();

    private PushId_AppRelshipDao pard=new PushId_AppRelshipDaoImpl();

    private HandlerDao hd = new HandlerDaoImpl();

    public void setPusher(PushHandler pusher)
    {
        this.pusher = pusher;
    }

    public void setPard(PushId_AppRelshipDao pard)
    {
        this.pard = pard;
    }

    public void setHd(HandlerDao hd)
    {
        this.hd = hd;
    }

    @Override
    public String push(Map<String, Object> map)
    {
        if(map==null||map.isEmpty()||!map.containsKey("appName"))
        {
            return null;
        }
        String appName;
        ConcurrentMap<String, Object> resultMap;
        try
        {
            appName=(String)map.get("appName");
            if(appName!=null&&"".equals(appName.trim()))
            {
                return null;
            }
            if (pard.isHave(appName)){
                File file = new File("/tmp/hls/" + appName + ".m3u8");
                if (file.exists()){
                    return null;
                }else {
                    pard.delete(appName);
                }
            }
            resultMap = pusher.push(map);
            // 生成一个标识该命令行线程集的key
            String pushId = UUID.randomUUID().toString();
            hd.set(pushId, resultMap);
            pard.set(appName, pushId);
        }
        catch (Exception e)
        {
            // 暂时先写这样，后期加日志
            throw new RuntimeException("视频流发生异常");
        }
        return appName;
    }

    @Override
    public void closePush(String appName)
    {
        String pushId=null;
        if(pard.isHave(appName))
        {
            pushId= pard.getPushId(appName);
        }
        if (pushId!=null&&hd.isHave(pushId))
        {
            ConcurrentMap<String, Object> map = hd.get(pushId);
            //关闭两个线程
            ((OutHandler)map.get("error")).interrupt();
            ((OutHandler)map.get("info")).interrupt();
            //关闭命令主进程
            ((Process)map.get("process")).destroy();
            //删除处理器与线程对应关系表
            hd.delete(pushId);
            //删除应用名对应关系表
            pard.delete(appName);
        }
    }

    @Override
    public List<String> viewAppName()
    {
        return pard.getAll();
    }

    @Override
    public void myPush(Map<String, Object> map) throws IOException {
        pusher.push(map);
    }
}
