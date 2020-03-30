package cn.bytecloud.smartCommunity.node.dto;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.node.entity.*;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
public class AddNodeDto {

    @NotEmpty
    private String processId;

    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotNull
    private NodeType type;

    @NotEmpty
    private String webId;

    @NotNull
    private NodeAttribute attribute;

    @NotEmpty
    private List<NodeButton> buttons;

    @Length(max = 100)
    private String desc;

    @NotNull
    private Boolean uploadFlag;

    @NotNull
    private HandlerType handlerType;

    private String unitId;

    public Node toData() throws ByteException {
        Node node = new Node();
        node.setProcessId(processId);
        node.setName(name);
        node.setType(type);
        node.setButtons(buttons);
        node.setDesc(desc);
        node.setAttribute(attribute);
        node.setUploadFlag(uploadFlag);
        node.setHandlerType(handlerType);
        node.setUnitId(unitId);
        if (handlerType == HandlerType.CUSTOMIZE && EmptyUtil.isEmpty(unitId)) {
            throw new ByteException("组织机构id不能为空");
        }
        node.setWebId(webId);
        return node;
    }
}
