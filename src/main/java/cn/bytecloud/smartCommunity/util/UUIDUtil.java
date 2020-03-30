package cn.bytecloud.smartCommunity.util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 随机生成一个32位的字符串，
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
