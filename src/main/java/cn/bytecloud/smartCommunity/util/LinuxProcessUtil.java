package cn.bytecloud.smartCommunity.util;


import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.video.realpaly.service.RemoteShellExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class LinuxProcessUtil {

    /**
     * 获取指定进程的PID
     *
     * @param command
     * @return
     */
    public static String getPID(String command, String ip, String username, String password, String str) throws Exception {
        BufferedReader reader = null;
        try {
            // 显示进程
            RemoteShellExecutor executor = new RemoteShellExecutor(ip, username, password);
            String exec = executor.exec("ps -ef|grep ffmpeg");
            System.out.println();
            String[] split = exec.split(str);
            for(int i = 0; i < split.length; i++){
                if (split[i].indexOf(command) != -1){
                    String[] split1 = split[i].split("\\s+");
                    log.info("获取PID为" + split1[1]);
                    return split1[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        log.info("找不到有关进程pid返回null");
        return null;
    }

    /**
     * 关闭Linux进程
     *
     * @param Pid 进程的PID
     */
    public static void closeLinuxProcess(String Pid) {
        Process process = null;
        BufferedReader reader = null;
        try {
            // 杀掉进程
            log.info("准备执行 kill -9 " + Pid);
            process = Runtime.getRuntime().exec("kill -9 " + Pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println("kill PID return info -----> " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }


}
