package cn.bytecloud.smartCommunity.menu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * 版权 @Copyright: 2019 www.bytecloud.cn Inc. All rights reserved.
 * 文件名称： MenuDaoImpl
 * 包名：cn.bytecloud.smartCommunity.menu.dao
 * 创建人：@author wangkn@bytecloud.cn
 * 创建时间：2019/07/05 11:21
 * 修改人：wangkn@bytecloud.cn
 * 修改时间：2019/07/05 11:21
 * 修改备注：
 */
@Repository
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private MenuRepository repository;

    @Autowired
    private MongoTemplate template;



}
