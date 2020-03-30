
package cn.bytecloud.smartCommunity.video.capturePicture.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.FileUtil;
import cn.bytecloud.smartCommunity.video.capturePicture.service.CaptureService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Calendar;

import static cn.bytecloud.smartCommunity.video.capturePicture.controller.CapturePictureController.API;

@RestController
@RequestMapping(API)
@Menu("视频监控")
public class CapturePictureController{

    public static final String API = "/api/video/";

    @Autowired
    private CaptureService captureService;

    @GetMapping(value = "capture",name = "截图")
    @RequiresPermissions((API+"capture"))
    public void capture(@RequestParam String id,HttpServletResponse response) throws ByteException{

        String fileName = captureService.capture(id);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + "capture.jpg");

        File file = new File(fileName);
        int t = 1000;
        int start = (int) Calendar.getInstance().getTimeInMillis();
        int end = (int) ((Calendar.getInstance().getTimeInMillis() - start));
        for (int i = 10000;i>end;end+=t){
            try {
                Thread.sleep(t);
                if (file.exists()){
                    FileInputStream in = new FileInputStream(file);
                    ServletOutputStream out = response.getOutputStream();

                    byte[] bytes = new byte[1024];
                    int num;
                    while ((num = in.read(bytes)) != -1) {
                        out.write(bytes, 0, num);
                    }
                    in.close();
                    out.close();
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        throw new ByteException("截图失败！");
    }


}
