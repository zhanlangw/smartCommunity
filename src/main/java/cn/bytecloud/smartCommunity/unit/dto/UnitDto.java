package cn.bytecloud.smartCommunity.unit.dto;

import cn.bytecloud.smartCommunity.unit.entity.Unit;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 组织机构Dto
 */
@Data
public class UnitDto {

    /**
     * 名称
     */
    @NotEmpty
    @Length(max = 20)
    private String name;


    @NotNull
    private Integer num;
    /**
     * 简称
     */
    @Length(max = 2,min = 2)
    private String abbre;

    /**
     * 办公地点
     */
    @Length(max = 100)
    private String address;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 父亲节点
     */
    private String pid;

    /**
     * 备注
     */
    @Length(max = 100)
    private String desc;

    /**
     * dto 转 entity
     */
    public Unit toEntity() {
        Unit unit = new Unit();
        unit.setName(name);
        unit.setAbbre(abbre);
        unit.setNum(num);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setPid(pid);
        unit.setDesc(desc);
        return unit;
    }


}
