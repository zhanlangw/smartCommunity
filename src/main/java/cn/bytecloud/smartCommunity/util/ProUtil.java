package cn.bytecloud.smartCommunity.util;


import java.io.*;
import java.util.Properties;

public class ProUtil {

    public static String getProValue(String path, String key) {
        Properties pro = new Properties();
        try {
            InputStream is = ProUtil.class.getClassLoader().getResourceAsStream(path);
            pro.load(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro.getProperty(key);
    }
}
