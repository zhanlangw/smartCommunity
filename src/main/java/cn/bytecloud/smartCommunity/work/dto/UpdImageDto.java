package cn.bytecloud.smartCommunity.work.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdImageDto {

    @NotEmpty
    private String workId;

    //处理前照片地质集合
    @Size(min = 1, max = 5)
    @NotNull
    private List<String> beforeImagePaths;

    //处理前视屏地质集合
    private List<String> beforeVideoPaths;
}
