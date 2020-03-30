package cn.bytecloud.smartCommunity.unit.service;


import cn.bytecloud.smartCommunity.base.dto.TreeDto;
import cn.bytecloud.smartCommunity.base.dto.TreeType;
import cn.bytecloud.smartCommunity.base.entity.BaseEntity;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.device.entity.Device;
import cn.bytecloud.smartCommunity.device.service.DeviceService;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.exception.ErrorCode;
import cn.bytecloud.smartCommunity.permission.dto.PermissionTree;
import cn.bytecloud.smartCommunity.unit.dao.UnitDao;
import cn.bytecloud.smartCommunity.unit.dao.UnitRepository;
import cn.bytecloud.smartCommunity.unit.dto.UnitDto;
import cn.bytecloud.smartCommunity.unit.dto.UpdUnitDto;
import cn.bytecloud.smartCommunity.unit.entity.Unit;
import cn.bytecloud.smartCommunity.user.entity.User;
import cn.bytecloud.smartCommunity.user.service.UserService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.MatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UnitServiceImpl implements UnitService {


    @Autowired
    private UnitDao dao;

    @Autowired
    private UnitRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 添加组织机构
     *
     * @param dto
     * @return dto:前台展示的对象
     * @throws ByteException
     */
    @Override
    public Map add(UnitDto dto) throws ByteException {
        if (EmptyUtil.isNotEmpty(dto.getTelephone()) && Arrays.stream(dto.getTelephone().split(",")).anyMatch(telephone -> !MatchUtil.checkTelephone(telephone))) {
            throw new ByteException("电话号码不符合要求");
        }
        List<Unit> units = repository.findByPid(dto.getPid());
        for (Unit unit : units) {
            if (unit.getName().equals(dto.getName())) {
                throw new ByteException("该名称已存在", ErrorCode.FAILURE);
            }
        }
        if ((!SystemConstant.INIT_UNIT_NAME.equals(dto.getName())) && EmptyUtil.isEmpty(dto.getPid())) {
            throw new ByteException("pid为空!");
        }

        if (null != repository.findByAbbre(dto.getAbbre())) {
            throw new ByteException("简称已经存在!!");
        }
        Unit unit = dao.save(dto.toEntity());
        return unit.toDto();
    }

    /**
     * 详情
     *
     * @param id 组织机构id
     * @return dto
     */
    @Override
    public Map item(String id) {
        return repository.findOne(id).toDto();
    }


    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) throws ByteException {
        //判断其是否是某组织机构的父类.
        List<Unit> units = repository.findByPid(id);
        if (0 < units.size()) {
            throw new ByteException("该节点是父亲节点,不可删除", ErrorCode.FAILURE);
        }
        if (userService.findAllByUnitId(id).size() > 0) {
            throw new ByteException("该部门下存在用户,禁止删除");
        }
        Unit unit = repository.findOne(id);
        //不允许删除初始化节点
        if (EmptyUtil.isEmpty(unit.getPid())) {
            throw new ByteException("禁止删除", ErrorCode.FAILURE);
        }
        repository.delete(id);
    }

    /**
     * 修改
     *
     * @param :UpdUnitDto
     * @return :Map
     */
    @Override
    public Map upd(UpdUnitDto updto) throws ByteException {
        if (EmptyUtil.isNotEmpty(updto.getTelephone()) && Arrays.stream(updto.getTelephone().split(",")).anyMatch(telephone -> !MatchUtil.checkTelephone(telephone))) {
            throw new ByteException("电话号码不符合要求");
        }
        if (updto.getId().equals(updto.getPid())) {
            throw new ByteException("父节点不能为自己");
        }
        if (findAllByPid(updto.getId()).contains(updto.getPid())) {
            throw new ByteException("父节点不能为自己的子节点");
        }
        List<Unit> units = repository.findByPid(updto.getPid());
        //修改的时候要把自己的名字排除在外并且同一级下不可以有重复的名字
        for (Unit unit : units) {
            if (unit.getName().equals(updto.getName()) && !updto.getId().equals(unit.getId())) {
                throw new ByteException("同一组织机构下该名称已存在", ErrorCode.FAILURE);
            }
        }
        if (EmptyUtil.isNotEmpty(repository.findOne(updto.getId()).getPid()) && EmptyUtil.isEmpty(updto.getPid())) {
            throw new ByteException("pid不能为空");
        }
        Unit abbre = repository.findByAbbre(updto.getAbbre());
        if (null != abbre && !abbre.getId().equals(updto.getId())) {
            throw new ByteException("简称已经存在!!");
        }
        return dao.upd(updto);
    }

    @Override
    public List<String> findAllByPid(String id) {
        List<String> list = findByPid(id).stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<String> data = new ArrayList<>(list);
        for (String unitId : list) {
            data.addAll(findAllByPid(unitId));
        }
        return data;
    }

    @Override
    public List<Unit> findAll() {
        return repository.findAll();
    }

    @Override
    public Object appTree( String id) {
        List<TreeDto> treeDtoList = new ArrayList<>();
        List<Unit> units;

        if (EmptyUtil.isEmpty(id)) {
            id = dao.findByPid(null).get(0).getId();
        }
        units = dao.findByPid(id);

        for (Unit unit : units) {
            TreeDto treeDto = new TreeDto();
            List<Unit> sonUnits = dao.findByPid(unit.getId());
            List<User> sonUsers = userService.findByUnitId(unit.getId());
            //如果他的子节点仍然有子节点,那么就设置isLeaf属性为false
            if ((sonUnits.size() + sonUsers.size()) > 0) {
                treeDto.setIsLeaf(false);
            } else {
                treeDto.setIsLeaf(true);
            }
            treeDto.setKey(unit.getId());
            treeDto.setType(TreeType.UNIT);
            treeDto.setTitle(unit.getName());
            treeDto.setUid(unit.getPid());
            treeDtoList.add(treeDto);
        }

        String finalId = id;
        userService.findByUnitId(id).forEach(user -> {
            if ((!user.getId().equals(SystemConstant.INIT_SYSTEM_ID)) && (!user.getUsername().equals(SystemConstant.INIT_ROOT_USERNAME))) {
                TreeDto treeDto = new TreeDto();
                List<User> list = userService.findByUnitId(finalId);
                //如果他的子节点仍然有子节点,那么就设置isLeaf属性为false
                if (0 < list.size()) {
                    treeDto.setIsLeaf(true);
                } else {
                    treeDto.setIsLeaf(false);
                }
                treeDto.setKey(user.getId());
                treeDto.setType(TreeType.USER);
                treeDto.setTitle(user.getName());
                treeDto.setUid(finalId);
                treeDtoList.add(treeDto);
            }
        });
        return treeDtoList;
    }

    /**
     * 树状结构展示
     *
     * @param :id
     * @return :List
     */
    @Override
    public List<TreeDto> tree(final String id, boolean userFlag) {
        List<TreeDto> treeDtoList = new ArrayList<>();
        List<Unit> units;

        units = dao.findByPid(id);

        for (Unit unit : units) {
            TreeDto treeDto = new TreeDto();
            List<Unit> sonUnits = dao.findByPid(unit.getId());
            List<User> users = new ArrayList<>();
            if (userFlag) {
                users = userService.findByUnitId(unit.getId());
            }
            //如果他的子节点仍然有子节点,那么就设置isLeaf属性为false
            if ((sonUnits.size() + users.size()) > 0) {
                treeDto.setIsLeaf(false);
            } else {
                treeDto.setIsLeaf(true);
            }
            treeDto.setKey(unit.getId());
            treeDto.setType(TreeType.UNIT);
            treeDto.setTitle(unit.getName());
            treeDto.setUid(unit.getPid());
            treeDtoList.add(treeDto);
        }

        if (userFlag) {
            userService.findByUnitId(id).forEach(user -> {
                if ((!user.getId().equals(SystemConstant.INIT_SYSTEM_ID)) && (!user.getUsername().equals(SystemConstant.INIT_ROOT_USERNAME))) {
                    TreeDto treeDto = new TreeDto();
                    List<User> list = userService.findByUnitId(id);
                    //如果他的子节点仍然有子节点,那么就设置isLeaf属性为false
                    if (0 < list.size()) {
                        treeDto.setIsLeaf(true);
                    } else {
                        treeDto.setIsLeaf(false);
                    }
                    treeDto.setKey(user.getId());
                    treeDto.setType(TreeType.USER);
                    treeDto.setTitle(user.getName());
                    treeDto.setUid(id);
                    treeDtoList.add(treeDto);
                }
            });
        }
        return treeDtoList;
    }

    /**
     * 通过id查询该组织名字
     *
     * @param :id
     * @return :String
     */
    @Override
    public String findNameById(String id) {
        return repository.findOne(id).getName();
    }

    /**
     * 通过id查询该组织机构
     *
     * @param :id
     * @return :Unit
     */
    @Override
    public Unit findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * 保存组织机构
     *
     * @param :Unit
     * @return :Unit
     */
    @Override
    public Unit save(Unit unit) {
        return repository.save(unit);
    }

    /**
     * 根据id查询
     *
     * @param unitId
     * @return
     */
    @Override
    public Unit findById(String unitId) {
        return repository.findOne(unitId);
    }

    @Override
    public Object deivceTree() {
        List<Unit> firstUnit = repository.findByPid(null);
        List<PermissionTree> data = new ArrayList<>();
        firstUnit.forEach(unit -> {
            data.add(toTree(unit));
        });
        return data;
    }

    @Override
    public List<Unit> findByPid(String pid) {
        return dao.findByPid(pid);
    }


    private PermissionTree toTree(Unit unit) {
        PermissionTree tree = toData(unit);
        tree.getTree().addAll(findByUnitId(unit.getId()));
        for (Unit u : dao.findByPid(unit.getId())) {
            tree.getTree().add(toTree(u));
        }

        return tree;
    }

    private List<PermissionTree> findByUnitId(String unitId) {

        List<PermissionTree> list = new ArrayList<>();
        deviceService.findByUnitId(unitId).forEach((Device device) -> {
            PermissionTree permissiontree = toData(device);
            list.add(permissiontree);
        });
        return list;
    }

    private PermissionTree toData(Unit menu) {
        PermissionTree data = new PermissionTree();
        data.setId(menu.getId());
        data.setName(menu.getName());
        data.setType(1);
        return data;
    }

    public PermissionTree toData(Device device) {
        PermissionTree data = new PermissionTree();
        data.setId(device.getId());
        data.setName(device.getName());
        data.setType(2);
        return data;
    }

}
