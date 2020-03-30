package cn.bytecloud.smartCommunity.path.dto;

import cn.bytecloud.smartCommunity.path.entity.Path;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdPathDto extends AddPathDto {
    @NotEmpty
    private String id;

    @Override
    public Path toData() {
        Path path = super.toData();
        path.setId(id);
        return path;
    }
}
