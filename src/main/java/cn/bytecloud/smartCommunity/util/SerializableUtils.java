package cn.bytecloud.smartCommunity.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import org.apache.shiro.session.Session;

public class SerializableUtils {

    /**
     * 将Session序列化成String类型
     */
    public static String serializ(Session session) {
        try {
            //ByteArrayOutputStream 用于存储序列化的Session对象
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //将Object对象输出成byte数据
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(session);

            //将字节码，编码成String类型数据
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("序列化失败");
        }
    }


    /**
     * 将一个Session的字符串序列化成字符串,反序列化
     */
    public static Session deserializ(String sessionStr) {
        try {
            if (EmptyUtil.isEmpty(sessionStr)) {
                return null;
            }
            //读取字节码表
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(sessionStr));

            //将字节码反序列化成 对象
            ObjectInputStream in = new ObjectInputStream(bis);
            Session session = (Session) in.readObject();
            return session;
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败");
        }
    }

}
