package cn.bytecloud.smartCommunity.fence.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;
@Data
@Document(collection = T_FENCE)
public class Fence extends BaseEntity {
    @Field(FENCE_NAME)
    private String name;

    @Field(FENCE_NUM)
    private List<Integer> num;

    @Field(FENCE_TYPE)
    private List<String> type;

    @Field(FENCE_DEVICE_ID)
    private String deviceId;

}
