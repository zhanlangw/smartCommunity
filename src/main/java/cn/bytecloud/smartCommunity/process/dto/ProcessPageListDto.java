package cn.bytecloud.smartCommunity.process.dto;

import cn.bytecloud.smartCommunity.util.StringUtil;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessPageListDto {
    private String id;
    private String name;
    private String desc;
    private long createTime;

    public String getCreateTime() {
        return StringUtil.getTime(new Date(createTime));
    }
}
