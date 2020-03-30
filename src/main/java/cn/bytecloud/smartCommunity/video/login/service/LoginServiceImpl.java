package cn.bytecloud.smartCommunity.video.login.service;

import cn.bytecloud.smartCommunity.device.dto.DeviceItemDto;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import com.netsdk.demo.module.LoginModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private DeviceService service;

    static {
        LoginModule.init((lLong, s, i, pointer) -> {
        }, (lLong, s, i, pointer) -> {
        });
    }

    @Override
    public Boolean login(String id) throws ByteException {
        if (LoginModule.m_hLoginHandle.longValue() != 0){
            loginOut();
        }
        DeviceItemDto itemDto = service.item(id);
        String ip = itemDto.getIp();
        Integer port = itemDto.getTcpPort();
        String username = itemDto.getUsername();
        String password = itemDto.getPassword();

        return LoginModule.login(ip, port, username, password);
    }

    @Override
    public void loginOut() throws ByteException {
        LoginModule.logout();
    }
}
