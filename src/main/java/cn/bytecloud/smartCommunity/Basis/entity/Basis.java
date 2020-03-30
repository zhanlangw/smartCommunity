package cn.bytecloud.smartCommunity.Basis.entity;

import cn.bytecloud.smartCommunity.constant.ModelConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Data
@Document(collection =T_BASIS)
public class Basis {
    @Id
    @NotEmpty
    private String id;

    @NotEmpty
    @Length(max = 20)
    @Field(BASIS_NAME)
    private String name;

    @NotNull
    @Field(BASIS_TIME_OUT)
    private Long timeOut;

    @NotEmpty
    @Field(BASIS_IMAGE_PATH)
    private String imagePath;

    @NotNull
    @Field(BASIS_FILE_MAX_SIZE)
    private Long fileMaxSize;

    //维度
//    @NotEmpty
    @Field(BASIS_LATITUDE)
    private String latitude;

    //经度
//    @NotEmpty
    @Field(BASIS_LONGITUDE)
    private String longitude;

    @Field(BASIS_SOUND_NUM)
    private Integer soundNum;

    @Field(BASIS_SOUND_FLAG)
    private Boolean soundFlag;

    public Basis(){
        this.soundFlag = false;
    }
}
