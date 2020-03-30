package cn.bytecloud.smartCommunity.util;

import cn.bytecloud.smartCommunity.user.entity.User;
import org.apache.shiro.SecurityUtils;

public class UserUtil {
    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getUserId() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
        //return UUIDUtil.getUUID();
    }
}
