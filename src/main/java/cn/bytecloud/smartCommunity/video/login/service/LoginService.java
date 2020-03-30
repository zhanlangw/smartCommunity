package cn.bytecloud.smartCommunity.video.login.service;

import cn.bytecloud.smartCommunity.exception.ByteException;

public interface LoginService {

    Boolean login(String id) throws ByteException;

    void loginOut() throws ByteException;

}
