package cn.bytecloud.smartCommunity.file.service;

import cn.bytecloud.smartCommunity.Basis.service.BasisService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.util.FileUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;


@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private BasisService basisService;

    @Override
    public String saveFileEntity(HttpServletRequest request) throws ByteException,
            FileUploadException, IOException {
        Long fileMaxSize = basisService.findFirst().getFileMaxSize();
        if (request.getContentLengthLong() > fileMaxSize * 1024 * 1024) {
            throw new ByteException("文件不能超过" + fileMaxSize + "MB");
        }
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        List<FileItem> files = servletFileUpload.parseRequest(request);
        FileItem file = null;
        for (FileItem file1 : files) {
            if (file1.getFieldName().equals("file")) {
                file = file1;
            }
        }

        String uuid = UUIDUtil.getUUID();
        InputStream in = file.getInputStream();
        String dataPath = "/" + uuid + "/" + file.getName();

        String path = FileUtil.getProjectPath() + dataPath;

        log.info("文件保存路径++" + path);
        File saveFile = new File(path);
        if (saveFile.exists() && saveFile.isFile()) {
            saveFile.delete();
            saveFile = new File(path);
        }

        // 判断路径是否存在,如果不存在就创建文件路径
        if (!saveFile.getParentFile().exists()) {
            final boolean mkdirs = saveFile.getParentFile().mkdirs();
        }

        // 将上传文件保存到一个目标文件当中
        OutputStream out = new FileOutputStream(saveFile);

        byte[] bytes = new byte[1024];
        int num;
        while ((num = in.read(bytes)) != -1) {
            out.write(bytes, 0, num);
        }
        in.close();
        out.close();

        //压缩图片
        int index = file.getName().lastIndexOf(".");
        String compressionPath = FileUtil.getProjectPath() + "/" + uuid + "/" + file.getName().substring(0, index) + "_compression.jpg";
        try {
            Thumbnails.of(new File(path)).scale(1f).outputQuality(0.15f).toFile(compressionPath);
            log.info("图片压缩成功,压缩后地址:" + compressionPath);
        } catch (Exception e) {
            try {
                byte[] b = new byte[1024];
                int n;
                InputStream inputStream = new FileInputStream(path);
                OutputStream outputStream = new FileOutputStream(compressionPath);
                while ((n = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, n);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e1) {
                log.info("图片压缩失败,原来文件:" + path);
            }
            log.info("图片压缩成功,压缩后地址:" + compressionPath);
        }
        return "/uploads" + dataPath;

    }

    public long getMaxFileSize(Integer fileType) throws ByteException {
        switch (fileType) {
            case 1://ppt
            case 2://doc
            case 3://video
                return 521 * 1024 * 1024;
            case 4://图片
                return 10 * 1024 * 1024;
            default:
                throw new ByteException(ErrorCode.FILE_SIZE_ERROR, "禁止修改");
        }
    }

    @Override
    public boolean deleteFile(String path) throws ByteException {
        File file = new File(FileUtil.getdeletePath(path));
        boolean exists = file.exists();
        boolean flag = file.isFile();
        if (flag && exists) {
            return file.delete();
        } else {
            throw new ByteException(ErrorCode.FAILURE);
        }

    }

    @Override
    public String upload(HttpServletRequest request) throws ByteException,
            IOException, FileUploadException {
        return saveFileEntity(request);
    }
}
