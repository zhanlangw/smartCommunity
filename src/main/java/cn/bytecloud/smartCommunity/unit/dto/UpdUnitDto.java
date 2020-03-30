package cn.bytecloud.smartCommunity.unit.dto;

import cn.bytecloud.smartCommunity.unit.entity.Unit;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 修改用dto
 */
@Data
public class UpdUnitDto extends UnitDto {


    /**
     * 修改需要传入的id
     */
    @NotEmpty
    @Length(max = 40)
    private String id;

    @Override
    public Unit toEntity() {
        Unit unit = super.toEntity();
        unit.setId(id);
        return unit;
    }
}
