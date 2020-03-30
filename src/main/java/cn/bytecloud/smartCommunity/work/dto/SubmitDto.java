package cn.bytecloud.smartCommunity.work.dto;

import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubmitDto {
    @NotEmpty
    private String id;
    @NotEmpty
    private String pathId;
    @NotEmpty
    private String todoId;

    private String desc;

    private Long time;

    private TimeType timeType;

    private List<String> userIds = new ArrayList<>();
    private List<String> unitIds = new ArrayList<>();

    private List<String> afterImagePaths;

    private List<String> afterVideoPaths;
}
