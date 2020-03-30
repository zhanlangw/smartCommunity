package cn.bytecloud.smartCommunity.util;


public class DirectoryUtil {

    public static String getProjectDir() {

        // 文件保存路径
        String curDir = Thread.currentThread().getContextClassLoader().getResource("/").getPath();

        curDir = curDir.replace("/WEB-INF/classes/", "");
        curDir = curDir.substring(0, curDir.lastIndexOf("/"));

        return curDir;

    }

}
