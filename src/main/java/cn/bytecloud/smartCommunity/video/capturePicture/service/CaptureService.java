package cn.bytecloud.smartCommunity.video.capturePicture.service;

import cn.bytecloud.smartCommunity.exception.ByteException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CaptureService {

    String capture(String id) throws ByteException;

}
