package cn.bytecloud.smartCommunity.stats.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = ModelConstant.T_STATS)
public class Stats extends BaseEntity {
    @Field(ModelConstant.STATS_YEAR)
    private Integer year;

    @Field(ModelConstant.STATS_MONTH)
    private Integer month;

    @Field(ModelConstant.STATS_DAY)
    private Integer day;

    @Field(ModelConstant.STATS_USER_ONLINE)
    private double userOnline;

    @Field(ModelConstant.STATS_UNIT_ID)
    private String unitId;
}
