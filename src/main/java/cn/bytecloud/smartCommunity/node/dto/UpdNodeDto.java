package cn.bytecloud.smartCommunity.node.dto;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.entity.Node;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdNodeDto extends AddNodeDto {
    @NotEmpty
    private String id;

    @Override
    public Node toData() throws ByteException {
        Node node = super.toData();
        node.setId(id);
        return node;
    }
}
