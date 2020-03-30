package cn.bytecloud.smartCommunity.video.realpaly.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutHandler extends  Thread {

    // 控制状态
    private volatile boolean status = true;

    private BufferedReader br;

    private String type;

    public OutHandler(InputStream is, String type)
    {
        br = new BufferedReader(new InputStreamReader(is));
        this.type = type;
    }


    /**
     * 执行输出线程
     */
    @Override
    public void run()
    {
        while (status)
        {
            boolean interrupted = this.isInterrupted();
            if (interrupted){
                break;
            }

        }
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            return;
        }

    }


}
