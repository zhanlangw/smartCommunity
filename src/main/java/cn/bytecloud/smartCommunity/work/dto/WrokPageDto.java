package cn.bytecloud.smartCommunity.work.dto;

import cn.bytecloud.smartCommunity.base.dto.BasePageDto;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkStatus;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WrokPageDto extends BasePageDto{
    private String title;
    private String num;
    private WorkSource source;
    private WorkStatus status;
    private String categoryName;
    private WorkType workType;

    private String filterField;

    private String searchField;

    private TodoType todoType;

    private boolean popupFlag;
}
