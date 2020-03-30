package cn.bytecloud.smartCommunity.menu.entity;

import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.MENU_NAME;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.MENU_PID;
import static cn.bytecloud.smartCommunity.constant.ModelConstant.T_MENU;

/**
 * 菜单实体
 */
@Document(collection = T_MENU)
@Data
public class Menu extends BaseEntity {


    /**
     * 菜单名称
     */
    @Field(MENU_NAME)
    private String name;

    @Field(MENU_PID)
    private String pid;
}
