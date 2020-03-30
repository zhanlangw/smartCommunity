package cn.bytecloud.smartCommunity.video.ptzControl.service;

import cn.bytecloud.smartCommunity.exception.ByteException;

public interface PtzControlService {

    void up(String id) throws ByteException;

    void down(String id) throws ByteException;

    void left(String id) throws ByteException;

    void right(String id) throws ByteException;


}
