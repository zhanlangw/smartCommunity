package cn.bytecloud.smartCommunity.process.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.process.dto.AddProcessDto;
import cn.bytecloud.smartCommunity.process.dto.ProcessPageDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessStyleDto;
import cn.bytecloud.smartCommunity.process.entity.Process;

import java.util.Map;

public interface ProcessService {
    Map add(AddProcessDto dto) throws ByteException;

    Map item(String id);

    Map upd(UpdProcessDto dto) throws ByteException;

    void del(String ids) throws ByteException;

    void updStyle(UpdProcessStyleDto dto);

    Object list(ProcessPageDto dto);

    Process findFirst();

    Process findById(String processId);

    Object ids(String id);
}
