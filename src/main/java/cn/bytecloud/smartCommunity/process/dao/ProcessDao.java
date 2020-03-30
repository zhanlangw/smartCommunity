package cn.bytecloud.smartCommunity.process.dao;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.process.dto.ProcessPageDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessStyleDto;
import cn.bytecloud.smartCommunity.process.entity.Process;

public interface ProcessDao {
    Process save(Process process);

    void updateStyle(UpdProcessStyleDto dto);

    PageModel<Process> list(ProcessPageDto dto);

    Process findFirst();

}
