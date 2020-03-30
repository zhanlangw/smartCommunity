package cn.bytecloud.smartCommunity.blacklist.dto;

import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AddBlacklistDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotEmpty
    private String typeId;

    @NotEmpty
    @Length(max = 100)
    private String imagePath;

    @Length(max = 100)
    private String leftImagePath;


    @Length(max = 100)
    private String rightImagePath;

    @Length(max = 100)
    private String desc;

    public Blacklist toData(){
        Blacklist blacklist = new Blacklist();
        blacklist.setName(name);
        blacklist.setTypeId(typeId);
        blacklist.setImagePath(imagePath);
        blacklist.setLeftImagePath(leftImagePath);
        blacklist.setRightImagePath(rightImagePath);
        blacklist.setDesc(desc);
        return blacklist;
    }
}
