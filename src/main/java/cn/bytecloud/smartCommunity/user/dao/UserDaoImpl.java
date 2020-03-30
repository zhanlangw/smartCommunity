package cn.bytecloud.smartCommunity.user.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.user.dto.*;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

/**
 *
 */
@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MongoTemplate template;


    /**
     * 保存用户
     *
     * @param :User
     * @return :User
     */
    @Override
    public User save(User user) {
        //新增
        if (EmptyUtil.isEmpty(user.getId())) {
            user.setId(UUIDUtil.getUUID());
            user.setCreateTime(System.currentTimeMillis());
            user.setCreatorId(UserUtil.getUserId());
        } else {
            //修改
            User old = repository.findOne(user.getId());
            user.setCreateTime(old.getCreateTime());
            user.setCreatorId(old.getCreatorId());
        }
        user.setUpdateTime(System.currentTimeMillis());
        repository.save(user);
        return user;
    }

    /**
     * 重置密码
     *
     * @param :id:用户id
     * @param :password:新密码
     */
    @Override
    public void resetPasswrod(String id, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(id));
        Update update = new Update();
        update.set(ModelConstant.USER_PASSWORD, password);
        template.updateFirst(query, update, User.class);
    }


    /**
     * 修改用户(组织机构)
     *
     * @param :UpdUserInUnitDto
     * @return :UserItemInUnitDto
     */
    @Override
    public UserItemInUnitDto updInUnit(UpdUserInUnitDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(dto.getId()));
        Update update = new Update();
        update.set(USER_NAME, dto.getName());
        update.set(USER_NUM, dto.getNum());
        update.set(USER_USERNAME, dto.getUsername());
        update.set(USER_ROLE_IDS, dto.getRoleIds());
        update.set(USER_TYPE, dto.getUserType());
        update.set(USER_ADDRESS, dto.getAddress());
        update.set(USER_TELEPHONE, dto.getTelephone());
        update.set(USER_UNIT_ID, dto.getUnitIds());
        update.set(DESC, dto.getDesc());
        update.set(UPDATE_TIME, System.currentTimeMillis());
        template.updateMulti(query, update, User.class);
        User user = repository.findOne(dto.getId());
        return user.toItemInUnitDto();
    }

    /**
     * 修改用户
     *
     * @param :UpdUserDto
     * @return :UserItemDto
     */
    @Override
    public UserItemDto upd(UpdUserDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(dto.getId()));
        Update update = new Update();
        update.set(USER_NAME, dto.getName());
        update.set(USER_GENDER, dto.getGender());
        update.set(USER_BIRTHDAY, dto.getBirthday());
        update.set(USER_AGE, dto.getAge());
        update.set(USER_TELEPHONE, dto.getTelephone());
        update.set(USER_EMAIL, dto.getEmail());
        update.set(USER_IMAGE_PATH, dto.getImagePath());
        update.set(USER_ADDRESS, dto.getAddress());
        update.set(UPDATE_TIME, System.currentTimeMillis());
        template.updateMulti(query, update, User.class);
        User user = repository.findOne(dto.getId());
        return user.toItemDto();
    }

    @Override
    public List<User> findByRoleId(String roleId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(USER_ROLE_IDS).elemMatch(new Criteria().in(roleId)));
        return template.find(query, User.class);
    }

    @Override
    public List<User> findWorkerByUnitId(String unitId, UserType userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where(USER_UNIT_ID).is(unitId));
        query.addCriteria(Criteria.where(USER_ROLE_IDS).elemMatch(new Criteria().in(userType)));
        return template.find(query, User.class);
    }

    @Override
    public List<User> findByIds(List<String> handlerIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).in(handlerIds));
        return template.find(query, User.class);
    }

    @Override
    public List<User> findByUnitIds(List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(USER_UNIT_ID).in(unitIds));
        return template.find(query, User.class);
    }

    @Override
    public void updImage(String id, String image) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).is(id));
        Update update = new Update();
        update.set(ModelConstant.USER_IMAGE_PATH, image);
        template.updateMulti(query, update, User.class);
    }

    @Override
    public PageModel<User> list(PageUserDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_NAME).ne(SystemConstant.INIT_ROOT_USERNAME));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where(ModelConstant.USER_USERNAME).regex(StringUtil.translat(dto.getName())),
                Criteria.where(ModelConstant.USER_NAME).regex(StringUtil.translat(dto.getName()))
        ));
        query.with(new Sort(Sort.Direction.DESC, CREATE_TIME));
        return pageList(query, dto, null);
    }

    @Override
    public List<User> findByUnitId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_UNIT_ID).elemMatch(new Criteria().in(id)));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.USER_NUM));
        return template.find(query, User.class);
    }

    @Override
    public List<User> findByUserTypeAndUnitIds(UserType worker, List<String> unitIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_TYPE).is(worker));
        query.addCriteria(Criteria.where(ModelConstant.USER_UNIT_ID).in(unitIds));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.USER_NUM));
        return template.find(query, User.class);
    }

    @Override
    public List<User> findByUnitIdAndUserType(String unitId, UserType userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_UNIT_ID).elemMatch(new Criteria().in(unitId)));
        query.addCriteria(Criteria.where(USER_TYPE).is(userType));
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.USER_NUM));
        return template.find(query, User.class);
    }

    @Override
    public void updateSoundFlag(boolean flag) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(UserUtil.getUserId()));
        Update update = new Update();
        update.set(USER_SOUND_FLAG, flag);
        template.updateMulti(query, update, User.class);
    }
}
