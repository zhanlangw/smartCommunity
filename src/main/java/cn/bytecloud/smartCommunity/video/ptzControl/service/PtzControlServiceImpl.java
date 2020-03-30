package cn.bytecloud.smartCommunity.video.ptzControl.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.video.login.service.LoginService;
import com.netsdk.demo.module.LoginModule;
import com.netsdk.lib.NetSDKLib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PtzControlServiceImpl implements PtzControlService{

    @Autowired
    private LoginService loginService;

    private NetSDKLib.CFG_ENCODE_INFO info = new NetSDKLib.CFG_ENCODE_INFO();

    @Override
    public void up(String id) throws ByteException {
        if (LoginModule.m_hLoginHandle.longValue()==0){
            loginService.login(id);
        }
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_UP_CONTROL,
                0, 4, 0, 0);

        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_UP_CONTROL,
                0, 0, 0, 1);
    }

    @Override
    public void down(String id) throws ByteException {
        if (LoginModule.m_hLoginHandle.longValue()==0){
            loginService.login(id);
        }
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_DOWN_CONTROL,
                0, 4, 0, 0);
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_DOWN_CONTROL,
                0, 0, 0, 1);
    }

    @Override
    public void left(String id) throws ByteException {
        if (LoginModule.m_hLoginHandle.longValue()==0){
            loginService.login(id);
        }
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_LEFT_CONTROL,
                0, 4, 0, 0);
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_LEFT_CONTROL,
                0, 0, 0, 1);
    }

    @Override
    public void right(String id) throws ByteException {
        if (LoginModule.m_hLoginHandle.longValue()==0){
            loginService.login(id);
        }
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_RIGHT_CONTROL,
                0, 4, 0, 0);
        LoginModule.netsdk.CLIENT_DHPTZControlEx(LoginModule.m_hLoginHandle, info.nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_RIGHT_CONTROL,
                0, 0, 0, 1);
    }
}
