package cn.bytecloud.smartCommunity.blacklist.dto;

import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdBlacklistDto  extends AddBlacklistDto{
    @NotEmpty
    private String id;

    @Override
    public Blacklist toData() {
        Blacklist blacklist = super.toData();
        blacklist.setId(id);
        return blacklist;
    }
}
