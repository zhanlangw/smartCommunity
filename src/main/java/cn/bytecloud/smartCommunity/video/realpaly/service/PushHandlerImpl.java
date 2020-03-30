package cn.bytecloud.smartCommunity.video.realpaly.service;

import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.util.FileUtil;
import cn.bytecloud.smartCommunity.video.realpaly.dao.OutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class PushHandlerImpl implements PushHandler {

    @Override
    public ConcurrentMap<String, Object> push(Map<String, Object> paramMap)
            throws IOException
    {
        // 从map里面取数据，组装成命令
        String comm ;
        if (!paramMap.containsKey("username")){
            comm = getCapString(paramMap);
        }else {
            comm = getComm4Map(paramMap);
        }
//        ConcurrentMap<String, Object> resultMap;
        // 执行命令行

//        final Process proc = Runtime.getRuntime().exec(comm);
//        OutHandler errorGobbler = new OutHandler(proc.getErrorStream(), "Error");
//        OutHandler outputGobbler = new OutHandler(proc.getInputStream(), "Info");
//
//        errorGobbler.start();
//        outputGobbler.start();
//         返回参数
//        resultMap = new ConcurrentHashMap<>(16);
//        resultMap.put("info", outputGobbler);
//        resultMap.put("error", errorGobbler);
//        resultMap.put("process", proc);
//        return resultMap;
//        new Thread(()->{
//            try {
//                String line;
//                BufferedReader br= new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//                while (true) {
//                    while ((line = br.readLine()) != null) {
//                    }
//                    Thread.sleep(1000);
//                }
//            } catch (Exception e) {
//
//            }
//        }).start();
        RemoteShellExecutor executor = new RemoteShellExecutor(String.valueOf(paramMap.get("output")), String.valueOf(paramMap.get("user")), String.valueOf(paramMap.get("word")));
        try {
            executor.exec(paramMap.get("ffmpeg") + comm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getComm4Map(Map<String, Object> paramMap)
    {
        // -i：输入流地址或者文件绝对地址
        StringBuilder comm = new StringBuilder("ffmpeg -re -rtsp_transport tcp -i ");
        // 是否有必输项：输入地址，输出地址，应用名
        if (paramMap.containsKey("input") && paramMap.containsKey("output")
                && paramMap.containsKey("appName"))
        {

            comm.append("\"rtsp://").append(paramMap.get("username")).append(":").append(paramMap.get("password"));

            comm.append("@").append(paramMap.get("input"));

            comm.append("/cam/realmonitor?channel=1&subtype=").append(paramMap.get("subtype")).append("\" ");

            comm.append("-vcodec copy -acodec aac -r 25 -s 640*480  -f flv").append(" ");

            comm.append("\"rtmp://");

            comm.append(paramMap.get("output")+":").append(paramMap.get("rtmp")).append("/hls/");
            //发布的应用名
            comm.append(paramMap.get("appName")).append("\"");
            System.out.println(comm.toString());
            return comm.toString();
        }
        else
        {
            throw new RuntimeException("输入流地址参数错误！");
        }

    }

    @Override
    public String getCapString(Map<String, Object> map) throws IOException {

        Calendar now = Calendar.getInstance();

        StringBuilder str = new StringBuilder("ffmpeg -y -i ");
        if (map.containsKey("input")){
            str.append("rtmp://").append(map.get("input")+":").append(map.get("rtmp")).append("/hls/");
            str.append(map.get("fname")).append(" ");
            str.append("-ss 0 -f image2 -vframes 1 -s 1280*720").append(" ");
            StringBuilder output = new StringBuilder();
            output.append("/home/bytecloud/smart_community/fileData");
            output.append("/capture/").append(now.get(Calendar.YEAR)).append(now.get(Calendar.MONTH) + 1);
            output.append(now.get(Calendar.DAY_OF_MONTH));
            File file = new File(output.toString());
            if (!file.exists()){
                file.mkdirs();
            }
            str.append(output);
            str.append("/").append(map.get("fileId")).append(".jpg");
            return str.toString();
        }else {
            throw new RuntimeException("输入地址错误");
        }
    }
}
