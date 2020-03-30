package cn.bytecloud.smartCommunity.address.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AddressService {
    void add(MultipartFile file) throws IOException;

    Object getData();

    String findUnitIdByLatitudeAndLongitude(String latitude, String longitude) throws ByteException;

}
