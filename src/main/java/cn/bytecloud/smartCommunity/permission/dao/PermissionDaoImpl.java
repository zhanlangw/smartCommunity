package cn.bytecloud.smartCommunity.permission.dao;

import cn.bytecloud.smartCommunity.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.ID;

/**
 * 版权 @Copyright: 2019 www.bytecloud.cn Inc. All rights reserved.
 * 文件名称： PermissionDaoImpl
 * 包名：cn.bytecloud.smartCommunity.permission.dao
 * 创建人：@author wangkn@bytecloud.cn
 * 创建时间：2019/07/04 15:31
 * 修改人：wangkn@bytecloud.cn
 * 修改时间：2019/07/04 15:31
 * 修改备注：
 */
@Service
public class PermissionDaoImpl implements PermissionDao {

    @Autowired
    private PermissionRepository repository;

    @Autowired
    private MongoTemplate template;


    @Override
    public List<Permission> findByIds(Set<String> permissionIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).in(permissionIds));
        return template.find(query, Permission.class);
    }


}
