package cn.bytecloud.smartCommunity.user.service;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.file.service.FileService;
import cn.bytecloud.smartCommunity.permission.entity.Permission;
import cn.bytecloud.smartCommunity.permission.service.PermissionService;
import cn.bytecloud.smartCommunity.role.entity.Role;
import cn.bytecloud.smartCommunity.role.service.RoleService;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.unit.service.UnitService;
import cn.bytecloud.smartCommunity.user.dao.UserDao;
import cn.bytecloud.smartCommunity.user.dao.UserRepository;
import cn.bytecloud.smartCommunity.user.dto.*;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.entity.UserType;
import cn.bytecloud.smartCommunity.util.*;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private FileService fileService;


    /**
     * 新增用户
     *
     * @param :AddUserByUnitDto:通过组织机构新建用户Dto
     * @return :AddUserByUnitDto
     */
    @Override
    public UserItemInUnitDto add(AddUserInUnitDto dto) throws ByteException {

        if (EmptyUtil.isNotEmpty(dto.getTelephone()) && !MatchUtil.checkTelephone(dto.getTelephone())) {
            throw new ByteException("电话号码不正确!");
        }

        //要先把登录名和数据库中的名字做匹配,如果有的话则报异常
        User u = repository.findByUsername(dto.getUsername());
        if (null != u) {
            throw new ByteException("该登录帐号已存在,请更换帐号或用已有帐号登录", ErrorCode.FAILURE);
        }
        User user = dao.save(dto.toEntity());
        permissionToCache(user);
        return user.toItemInUnitDto();
    }

    /**
     * 组织机构下的个人信息展示
     */
    @Override
    public UserItemInUnitDto itemInUnit(String id) {
        return repository.findOne(id).toItemInUnitDto();
    }


    /**
     * 删除用户
     *
     * @param :id:指定用户id
     */
    @Override
    public void del(String id) throws ByteException {
        User user = repository.findOne(id);
        if ("root".equals(user.getUsername())) {
            throw new ByteException("禁止删除", ErrorCode.FAILURE);
        }
        repository.delete(id);
    }


    /**
     * 重置密码
     *
     * @param :ResetPasswordDto:重置密码Dto
     */
    @Override
    public void resetPasswrod(ResetPasswordDto dto) throws ByteException {
        if (!MatchUtil.checkPassword(dto.getPassword())) {
            throw new ByteException("密码必须由数字和英文组成", ErrorCode.PARAMETER);
        }
        dao.resetPasswrod(dto.getId(), MD5Util.getMD5(dto.getPassword()));
    }

    /**
     * 修改用户信息(组织机构)
     *
     * @param :UpdUserInUnitDto:
     * @return :UserItemInUnitDto
     */
    @Override
    public UserItemInUnitDto updInUnit(UpdUserInUnitDto dto) throws ByteException {
        if (EmptyUtil.isNotEmpty(dto.getTelephone()) && !MatchUtil.checkTelephone(dto.getTelephone())) {
            throw new ByteException("电话号码不正确!");
        }
        UserItemInUnitDto userItemInUnitDto = dao.updInUnit(dto);
        permissionToCache(repository.findOne(userItemInUnitDto.getId()));
        return userItemInUnitDto;
    }


    /**
     * 详情
     *
     * @param :id:指定用户id
     * @return :UserItemDto
     */
    @Override
    public UserItemDto item(String id) {
        return repository.findOne(UserUtil.getUserId()).toItemDto();
    }

    /**
     * 修改用户信息
     *
     * @param :UpdUserDto:
     * @return :UserItemDto
     */
    @Override
    public UserItemDto upd(UpdUserDto dto) throws ByteException {
        if (EmptyUtil.isNotEmpty(dto.getTelephone()) && !MatchUtil.checkTelephone(dto.getTelephone())) {
            throw new ByteException("电话号码不正确!");
        }
        if (EmptyUtil.isNotEmpty(dto.getEmail()) && !MatchUtil.checkEmail(dto.getEmail())) {
            throw new ByteException("邮箱不正确!");
        }
        return dao.upd(dto);
    }

    /**
     * 保存用户信息
     *
     * @param :User
     * @return :User
     */
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Map login(LoginDto dto) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(dto.getType() == 1 ? SystemConstant.LOGIN_SPLIT + dto.getUsername() : dto.getUsername(), MD5Util.getMD5(dto
                .getPassword()));
        token.setRememberMe(true);
        subject.login(token);
        User user = UserUtil.getUser();
        Map map = new HashedMap();
        map.put("username", user.getUsername());
        map.put("name", user.getName());
        map.put("id", user.getId());
        map.put("token", SecurityUtils.getSubject().getSession().getId());

        //把权限放入缓存中
        permissionToCache(user);
        return map;
    }

    /**
     * 把权限放入缓存中
     *
     * @param user
     */
    private void permissionToCache(User user) {
        ThreadPool.threadPool.execute(() -> {
            findPermission(user);
        });
    }

    @Override
    public User findById(String creatorId) {
        return repository.findOne(creatorId);
    }

    @Override
    public List<User> findByUnitId(String id) {
        return dao.findByUnitId(id);
    }

    @Override
    public List<User> findByRoleId(String id) {
        return dao.findByRoleId(id);
    }

    @Override
    public List<User> findWorkerByUnitId(String unitId) {
        return dao.findWorkerByUnitId(unitId, UserType.WORKER);
    }

    @Override
    public List<User> findByIds(List<String> handlerIds) {
        return dao.findByIds(handlerIds);
    }

    @Override
    public void updUserPermissionCache(String id) {
        dao.findByRoleId(id).forEach(this::permissionToCache);
    }

    @Override
    public Set<String> findPermission(User user) {
        Set<String> data;
        try {
            if (SystemConstant.INIT_ROOT_USERNAME.equals(user.getUsername()) || SystemConstant.INIT_ADMIN_USERNAME.equals(user.getUsername()) || UserType.ROOT == user
                    .getUserType() || UserType.ADMIN == user.getUserType()) {
                data = permissionService.findAll().stream().map(Permission::getInterfaceUrl).collect(Collectors.toSet());
            } else {
                data = permissionService.findByUserId(user).stream().map(Permission::getInterfaceUrl).collect(Collectors.toSet());
            }
            redisTemplate.opsForValue().set(user.getId(), data);
            redisTemplate.expire(user.getId(), 30, TimeUnit.DAYS);
        } finally {
            //释放连接
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return data;
    }

    @Override
    public List<User> findByUnitIdS(List<String> unitIds) {
        return dao.findByUnitIds(unitIds);
    }

    @Override
    public List<User> findAllByUnitIdAndUserType(String unitId, UserType userType) {
        List<User> list = new ArrayList<>(dao.findByUnitIdAndUserType(unitId, userType));
        unitService.findByPid(unitId).forEach(unit -> {
            list.addAll(findAllByUnitIdAndUserType(unit.getId(), userType));
        });
        return list;
    }

    @Override
    public List<User> findAllByUnitId(String unitId) {
        List<User> list = new ArrayList<>(dao.findByUnitId(unitId));
        unitService.findByPid(unitId).forEach(unit -> {
            list.addAll(findAllByUnitId(unit.getId()));
        });
        return list;
    }

    @Override
    public List<User> findByUserType(UserType userType) {
        return repository.findByUserType(userType);
    }

    @Override
    public List<User> findByUserTypeAndUnitIds(UserType worker, List<String> unitIds) {
        return dao.findByUserTypeAndUnitIds(worker, unitIds);
    }

    @Override
    public void openAlarmSound() {
        dao.updateSoundFlag(true);
    }

    @Override
    public void closeAlarmSound() {
        dao.updateSoundFlag(false);
    }

    @Override
    public boolean sound() {
        Boolean soundFlag = repository.findOne(UserUtil.getUserId()).getSoundFlag();
        return soundFlag == null ? true : soundFlag;
    }

    @Override
    public void logout() {
        redisTemplate.delete(UserUtil.getUserId());
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
    }

    @Override
    public void updImage(String id, String image) {
        dao.updImage(id, image);
    }

    @Override
    public Object list(PageUserDto dto) {
        PageModel data = dao.list(dto);
        data.setValue(EntityUtil.entityListToDtoList(data.getValue(), UserListDto.class));
        return data;
    }

    @Override
    public void importUsers(String path) throws ByteException, IOException {
        File file = new File(FileUtil.getdeletePath(path));
        String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Workbook workbook = null;

        //判断格式
        if ("xls".equalsIgnoreCase(type)) {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } else if ("xlsx".equalsIgnoreCase(type)) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            throw new ByteException("文件类型错误!");
        }
        Sheet sheet = workbook.getSheetAt(0);

        List<User> users = checkSheet(sheet);

        repository.save(users);

        fileService.deleteFile(path);
    }

    @Override
    public void updpassword(UpdPasswordDto dto) throws ByteException {
        User user = UserUtil.getUser();
        user = repository.findOne(user.getId());
        if (!MatchUtil.checkPassword(dto.getNewPasswrod())) {
            throw new ByteException("密码必须由数字和英文组成", ErrorCode.PARAMETER);
        }
        if (!user.getPassword().equals(MD5Util.getMD5(dto.getPassword()))) {
            throw new ByteException("旧密码错误!");
        }
        if (user.getPassword().equals(MD5Util.getMD5(dto.getNewPasswrod()))) {
            throw new ByteException("新密码不能和旧密码一致！！!");
        }
        dao.resetPasswrod(user.getId(), MD5Util.getMD5(dto.getNewPasswrod()));
    }

    private List<User> checkSheet(Sheet sheet) throws ByteException {
        List<User> data = new ArrayList<>();
        String userId = UserUtil.getUserId();

        List<String> usernames = new ArrayList<>();

        for (Row row : sheet) {

            // 一行数据 对应 一个区域对象
            if (row.getRowNum() == 0) {
                // 第一行 跳过
                continue;
            }

            User user = new User();
            String number = "第" + (row.getRowNum() + 1) + "行";


            Cell cell = row.getCell(0);
            if (cell == null) {
                throw new ByteException(number + "名称不能为空");
            }
            cell.setCellType(CellType.STRING);
            if (EmptyUtil.isEmpty(cell.getStringCellValue())) {
                throw new ByteException(number + "名称不能为空");
            } else {
                if (cell.getStringCellValue().length() > 20) {
                    throw new ByteException(number + "名称长度不能超过20");
                }
                user.setName(cell.getStringCellValue());
            }

            cell = row.getCell(1);
            if (cell == null) {
                throw new ByteException(number + "系统账号不能为空");
            }
            cell.setCellType(CellType.STRING);
            if (EmptyUtil.isEmpty(cell.getStringCellValue())) {
                throw new ByteException(number + "系统账号不能为空");
            } else {
                if (cell.getStringCellValue().length() > 20) {
                    throw new ByteException(number + "系统账号长度不能超过20");
                }
                User u = repository.findByUsername(cell.getStringCellValue());
                if (null != u || usernames.contains(cell.getStringCellValue())) {
                    throw new ByteException(number + "系统帐号已存在");
                }
                user.setUsername(cell.getStringCellValue());
                usernames.add(cell.getStringCellValue());
            }

            cell = row.getCell(2);
            if (cell == null) {
                throw new ByteException(number + "角色不能为空");
            }
            cell.setCellType(CellType.STRING);
            if (EmptyUtil.isEmpty(cell.getStringCellValue())) {
                throw new ByteException(number + "角色不能为空");
            } else {
                Set<String> names = Arrays.stream(cell.getStringCellValue().split(",")).collect(Collectors.toSet());
                Set<String> roleIds = new HashSet<>();
                for (String name : names) {
                    Role role = roleService.findFirstByName(name);
                    if (role == null) {
                        throw new ByteException(number + "角色" + name + "不存在!");
                    }
                    roleIds.add(role.getId());
                }
                user.setRoleIds(roleIds);
            }

            cell = row.getCell(3);
            if (cell == null) {
                throw new ByteException(number + "用户类型不能为空");
            }
            cell.setCellType(CellType.STRING);
            if (EmptyUtil.isEmpty(cell.getStringCellValue())) {
                throw new ByteException(number + "用户类型不能为空");
            } else {
                UserType type = UserType.getValue(cell.getStringCellValue());
                user.setUserType(type);
            }

            cell = row.getCell(4);
            if (cell == null) {
                throw new ByteException(number + "父亲节点不能为空");
            }
            cell.setCellType(CellType.STRING);
            if (EmptyUtil.isEmpty(cell.getStringCellValue())) {
                throw new ByteException(number + "父亲节点不能为空");
            } else {
                List<String> list = new ArrayList<>();
                for (String unitName : cell.getStringCellValue().split(",")) {
                    Unit unit = unitService.findByName(unitName);
                    if (unit == null) {
                        throw new ByteException(number + "父亲节点" + unitName + "不存在!");
                    }
                    list.add(unit.getId());
                }

                user.setUnitIds(list);
            }

            cell = row.getCell(5);
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                if (EmptyUtil.isNotEmpty(cell.getStringCellValue())) {
                    user.setAddress(cell.getStringCellValue());
                }
            }

            cell = row.getCell(6);
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                if (EmptyUtil.isNotEmpty(cell.getStringCellValue())) {
                    if (!MatchUtil.checkTelephone(cell.getStringCellValue())) {
                        throw new ByteException(number + "电话号码不正确!");
                    } else {
                        user.setTelephone(cell.getStringCellValue());
                    }

                }
            }
            user.setId(UUIDUtil.getUUID());
            user.setPassword(MD5Util.getMD5("123456"));
            user.setCreateTime(System.currentTimeMillis());
            user.setCreatorId(userId);
            data.add(user);
        }
        return data;
    }

    /**
     * 通过用户名查询用户
     *
     * @param :User
     * @return :User
     */
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
