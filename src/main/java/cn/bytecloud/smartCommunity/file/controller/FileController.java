package cn.bytecloud.smartCommunity.file.controller;

import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.file.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService service;

    @Autowired
    private BasisService basisService;

    @PostMapping("/upload")
    public APIResult upload(HttpServletRequest request) throws FileUploadException,
            IOException, ByteException, org.apache.commons.fileupload.FileUploadException, InterruptedException {
        return APIResult.success().setValue(service.upload(request));
    }

    @GetMapping("/del")
    public APIResult del(@RequestParam String path) throws ByteException {
        service.deleteFile(path);
        return APIResult.success();
    }
}
