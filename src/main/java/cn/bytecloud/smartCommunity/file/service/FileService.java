package cn.bytecloud.smartCommunity.file.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public interface FileService {

    public String saveFileEntity(HttpServletRequest request) throws IOException,
            FileUploadException, ByteException, org.apache.commons.fileupload.FileUploadException;

    public boolean deleteFile(String path) throws ByteException;

    String upload(HttpServletRequest request) throws FileUploadException, ByteException, IOException, org.apache.commons.fileupload.FileUploadException;
}
