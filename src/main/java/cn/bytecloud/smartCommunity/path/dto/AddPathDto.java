package cn.bytecloud.smartCommunity.path.dto;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.path.entity.Path;
import cn.bytecloud.smartCommunity.path.entity.PathAttribute;
import cn.bytecloud.smartCommunity.path.entity.PathType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class AddPathDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotNull
    private PathType type;

    @NotEmpty
    private String webId;

    @NotNull
    private PathAttribute attribute;

    @NotEmpty
    private String processId;

    @NotEmpty
    private String startNodeId;

    @NotEmpty
    private String endNodeId;

    private Long time;

    @Length(max = 100)
    private String desc;

    public Path toData() {
        Path path = new Path();
        path.setName(name);
        path.setType(type);
        path.setAttribute(attribute);
        path.setDesc(desc);
        path.setProcessId(processId);
        path.setStartNodeId(startNodeId);
        path.setEndNodeId(endNodeId);
        path.setWebId(webId);
        return path;
    }
}
