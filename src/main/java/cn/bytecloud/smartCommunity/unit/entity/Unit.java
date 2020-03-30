package cn.bytecloud.smartCommunity.unit.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 * 组织机构实体类
 */
@Data
@Document(collection = T_UNIT)
public class Unit extends BaseEntity {

    /**
     * 名称
     */
    @Field(UNIT_NAME)
    private String name;


    @Field(UNIT_NUM)
    private Integer num;

    /**
     * 简称
     */
    @Field(UNIT_ABBRE)
    private String abbre;

    /**
     * 办公地点
     */
    @Field(UNIT_ADDRESS)
    private String address;

    /**
     * 联系电话
     */
    @Field(UNIT_TELEPHONE)
    private String telephone;

    /**
     * 父亲节点
     */
    @Field(UNIT_PID)
    private String pid;

    /**
     * entity 转 dto
     */
    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("name", name);
        map.put("num", num);
        map.put("abbre", abbre);
        map.put("address", address);
        map.put("telephone", telephone);
        map.put("pid", pid);
        map.put("pName", EmptyUtil.isEmpty(pid) ? "" : SpringUtils.getBean(UnitService.class).findById(pid).getName());
        map.put("desc", super.getDesc());
        return map;
    }


}
