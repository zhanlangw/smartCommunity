package cn.bytecloud.smartCommunity.user;

import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.MD5Util;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MyAuthRealm extends AuthorizingRealm {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5("123456"));
    }

    @Override
    public String getName() {
        return "MyAuthRealm";
    }

    //支持UsernamePasswordToken
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //从token中获取用户身份信息
        String username = (String) token.getPrincipal();
        boolean webFlag = false;
        if (username.startsWith(SystemConstant.LOGIN_SPLIT)) {
            webFlag = true;
            username = username.substring(36);

        }
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException();
        }

//        if (webFlag && (user.getUserType() == UserType.WORKER || user.getUserType() == UserType.SUPERVISION_USER)) {
//            throw new AuthenticationException();
//        }
        //返回认证信息由父类AuthenticationRealm进行认证
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> data;
        try {
            data = (Set<String>) redisTemplate.opsForValue().get(UserUtil.getUserId());
        } finally {
            //释放连接
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }

        if (data == null || data.size() == 0) {
            data = userService.findPermission(UserUtil.getUser());
        }
        authorizationInfo.setStringPermissions(data);
        return authorizationInfo;
    }
}


