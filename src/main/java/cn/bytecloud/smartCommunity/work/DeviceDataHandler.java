package cn.bytecloud.smartCommunity.work;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.work.dto.AddWorkFromDeviceDto;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Service
public class DeviceDataHandler {

    private TaskThread instance = new TaskThread();

    private Thread thread = null;

    public TaskThread getInstance() {
        return instance;
    }


    public void execute() {
        if (thread == null) {
            thread = new Thread(this.getInstance());
            thread.setName("LineListener");
            thread.start();
        }
    }

    @Autowired
    private WorkService service;

    public class TaskThread
            implements Runnable {
        public ConcurrentHashMap<String, AddWorkFromDeviceDto> map = new ConcurrentHashMap<>();

        public void addData(String id, AddWorkFromDeviceDto dto) {
            map.put(id, dto);
        }

        @Override
        public void run() {
            log.info("队列监听启动！！");
            while (true) {
                Set<String> set = new HashSet<>();
                map.forEach((key,value)->{
                    set.add(key);
                    try {
                        service.add(value,value.getFlag());
                    } catch (ByteException e) {
                        e.printStackTrace();
                    }
                });
                for (String key : set) {
                    map.remove(key);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
