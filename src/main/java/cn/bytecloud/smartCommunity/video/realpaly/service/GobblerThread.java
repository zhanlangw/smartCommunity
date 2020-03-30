package cn.bytecloud.smartCommunity.video.realpaly.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GobblerThread extends Thread
{
    InputStream is;
    String type;

    GobblerThread(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
