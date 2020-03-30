package cn.bytecloud.smartCommunity.util;

public class PathUtil {
    public static String getProjectPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        for (int i = 0; i < 4; i++) {
            int lastIndexOf = path.lastIndexOf("/");
            path = path.substring(0, lastIndexOf);
        }
        return path + "/fileserver";
    }

    public static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return index == -1 ? path : path.substring(index + 1);
    }
}
