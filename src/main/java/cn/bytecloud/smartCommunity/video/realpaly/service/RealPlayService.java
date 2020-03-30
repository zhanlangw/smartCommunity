package cn.bytecloud.smartCommunity.video.realpaly.service;


import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.realpaly.dto.RealPlayDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RealPlayService {

    RealPlayDto start(String id, String type, HttpServletRequest request) throws ByteException;

    void close(String id,String type, HttpServletRequest request) throws ByteException;

    String aiPlay(String id) throws ByteException;

    String aiStop(String id) throws ByteException;

}
