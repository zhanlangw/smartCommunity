package cn.bytecloud.smartCommunity.config.shiro;

import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import org.apache.shiro.web.servlet.SimpleCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyCookle extends SimpleCookie {
    public static final String AUTHORIZATION = "Authorization";

    @Override
    public String readValue(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(AUTHORIZATION);
        if (EmptyUtil.isEmpty(header)) {
            header = UUIDUtil.getUUID();
        }
        super.setName(header);
        return header;
    }
}
