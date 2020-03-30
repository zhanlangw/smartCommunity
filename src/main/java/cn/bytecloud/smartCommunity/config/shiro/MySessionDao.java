package cn.bytecloud.smartCommunity.config.shiro;

import cn.bytecloud.smartCommunity.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Component
public class MySessionDao extends EnterpriseCacheSessionDAO {

    @Autowired
    private RedisTemplate redisTemplate;


    //创建session
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        setShiroSession(sessionId.toString(), session);
        return sessionId;
    }

    //获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            session = getShiroSession(sessionId.toString());
        }
        return session;
    }

    //更新session
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        setShiroSession(session.getId().toString(), session);
    }

    //删除session
    @Override
    protected void doDelete(Session session) {
        try {
            super.doDelete(session);
            redisTemplate.delete(session.getId().toString());
        } finally {
            //释放连接
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    private Session getShiroSession(String key) {
        return SerializableUtils.deserializ((String) redisTemplate.opsForValue().get(key));
    }

    private void setShiroSession(String key, Session session) {
        try {
            redisTemplate.opsForValue().set(key, SerializableUtils.serializ(session));
            redisTemplate.expire(key, 30, TimeUnit.DAYS);
        } finally {
            //释放连接
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }
}
