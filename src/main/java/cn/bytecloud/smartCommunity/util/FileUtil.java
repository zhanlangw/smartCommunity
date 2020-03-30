package cn.bytecloud.smartCommunity.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Slf4j
public class  FileUtil {

    // 创建目录
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            return true;
        } else {
            return false;
        }
    }

    public static FileItem getFile(HttpServletRequest request) throws FileUploadException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        List<FileItem> files = servletFileUpload.parseRequest(request);
        FileItem file = null;
        for (FileItem file1 : files) {
            if (file1.getFieldName().equals("file")) {
                file = file1;
            }
        }
        return file;
    }

    public static String getProjectPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        for (int i = 0; i < 4; i++) {
            int lastIndexOf = path.lastIndexOf("/");
            path = path.substring(0, lastIndexOf);
        }
        path = path.startsWith("file:") ? path.substring(5) : path;
//        log.info("文件路径++" + path);
        return path + "/fileData/uploads";
    }

    public static String getdeletePath(String p) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        for (int i = 0; i < 4; i++) {
            int lastIndexOf = path.lastIndexOf("/");
            path = path.substring(0, lastIndexOf);
        }
        path = path.startsWith("file:") ? path.substring(5) : path;
//        log.info("文件路径++" + path);
        return path + "/fileData" + p;
    }
}
