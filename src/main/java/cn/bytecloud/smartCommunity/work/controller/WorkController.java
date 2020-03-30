package cn.bytecloud.smartCommunity.work.controller;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.base.dto.APIResult;
import cn.bytecloud.smartCommunity.config.ThreadPool;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.todo.entity.TodoType;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.DeviceDataHandler;
import cn.bytecloud.smartCommunity.work.dto.*;
import cn.bytecloud.smartCommunity.work.service.WorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.smartCommunity.work.controller.WorkController.API;

@RestController
@RequestMapping(API)
@Menu("案卷管理")
public class WorkController {
    public static final String API = "/api/work/";

    @Autowired
    private WorkService service;

    @Autowired
    private DeviceDataHandler deviceDataHandler;

    @PostMapping(value = "add", name = "创建")
    @RequiresPermissions((API + "add"))
    public APIResult add(@RequestBody @Validated AddWorkDto dto) throws ByteException {
        service.add(dto);
        return APIResult.success();
    }

    /**
     * 摄像机产生案卷
     *
     * @param dto
     * @return
     * @throws ByteException
     */
    @PostMapping("fromdevice/add")
    public APIResult deviceAdd(@RequestBody @Validated AddWorkFromDeviceDto dto) throws ByteException {
        dto.setFlag(false);
        service.add(dto,false);
//        deviceDataHandler.getInstance().addData(UUIDUtil.getUUID(),dto);
        return APIResult.success();
    }

    /**
     * 摄像机模型产生案卷
     *
     * @param dto
     * @return
     * @throws ByteException
     */
    @PostMapping("fromdevice/model/add")
    public APIResult deviceModelAdd(@RequestBody @Validated AddWorkFromDeviceDto dto) throws ByteException {
        dto.setFlag(true);
        service.add(dto,true);
//        deviceDataHandler.getInstance().addData(UUIDUtil.getUUID(),dto);
        return APIResult.success();
    }

    @GetMapping(value = "item", name = "详情")
    @RequiresPermissions((API + "item"))
    public APIResult item(@RequestParam String id, @RequestParam Integer type) throws ByteException {
        return APIResult.success().setValue(service.item(id, type));
    }

    @GetMapping(value = "del", name = "已经处置删除")
    @RequiresPermissions(API + "del")
    public APIResult del(@RequestParam String ids) {
        service.del(ids);
        return APIResult.success();
    }

    @GetMapping(value = "todo/del", name = "待办删除")
    @RequiresPermissions(API + "todo/del")
    public APIResult todoDel(@RequestParam String ids) {
        service.todoDel(ids);
        return APIResult.success();
    }

    @GetMapping(value = "finish/del", name = "已办删除")
    @RequiresPermissions(API + "todo/del")
    public APIResult finishDel(@RequestParam String ids) {
        service.finishDel(ids);
        return APIResult.success();
    }

    /**
     * @param flag 是否通过审核
     * @param id   代办id
     * @throws ByteException
     */
    @GetMapping(value = "accept", name = "受理")
    @RequiresPermissions(API + "accept")
    public APIResult accept(@RequestParam boolean flag, @RequestParam String id) throws ByteException {
        service.accept(flag, id);
        return APIResult.success();
    }

    /**
     * 点击提交,返回可选路径
     *
     * @param id 代办id
     * @return
     */
    @GetMapping(value = "path", name = "获取提交路径")
    @RequiresPermissions(API + "path")
    public APIResult paths(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.getPaths(id));
    }

    /**
     * 点击提交,返回可选路径
     */
    @PostMapping(value = "submit", name = "提交")
    @RequiresPermissions(API + "submit")
    public APIResult submit(@RequestBody @Validated SubmitDto dto) throws ByteException {
        service.submit(dto);
        return APIResult.success();
    }

    /**
     * @param id 代办id
     * @return
     * @throws ByteException
     */
    @GetMapping(value = "end", name = "结束")
    @RequiresPermissions(API + "end")
    public APIResult submit(@RequestParam String id) throws ByteException {
        service.end(id);
        return APIResult.success();
    }

    /**
     * @return
     * @throws ByteException
     */
    @GetMapping(value = "withdraw", name = "撤回")
    @RequiresPermissions(API + "withdraw")
    public APIResult withdraw(@RequestParam String workId,@RequestParam String todoId, String pathId) throws ByteException {
        service.withdraw(workId,todoId,pathId);
        return APIResult.success();
    }

    @GetMapping(value = "todo/list", name = "待处置列表")
    @RequiresPermissions(API + "todo/list")
    @Menu("案卷列表")
    public APIResult todoList(@Validated WrokPageDto dto) throws ByteException {
        dto.setTodoType(TodoType.TODO);
        return APIResult.success().setValue(service.todoList(dto));
    }

    @GetMapping(value = "review/list", name = "待核查列表")
    @RequiresPermissions(API + "review/list")
    @Menu("案卷列表")
    public APIResult reviewList(@Validated WrokPageDto dto) throws ByteException {
        dto.setTodoType(TodoType.REVIEW);
        return APIResult.success().setValue(service.todoList(dto));
    }

    @GetMapping(value = "return/list", name = "退件列表")
    @RequiresPermissions(API + "return/list")
    @Menu("案卷列表")
    public APIResult returnList(@Validated WrokPageDto dto) throws ByteException {
        dto.setTodoType(TodoType.RETURN);
        return APIResult.success().setValue(service.todoList(dto));
    }

    @GetMapping(value = "delay/list", name = "申请延时列表")
    @RequiresPermissions(API + "delay/list")
    @Menu("案卷列表")
    public APIResult dalayList(@Validated WrokPageDto dto) throws ByteException {
        dto.setTodoType(TodoType.DELAY);
        return APIResult.success().setValue(service.todoList(dto));
    }

    @GetMapping(value = "finish/list", name = "已处置列表")
    @RequiresPermissions(API + "finish/list")
    @Menu("案卷列表")
    public APIResult finishList(@Validated WrokPageDto dto) throws ByteException {
        return APIResult.success().setValue(service.finishList(dto));
    }

    @GetMapping(value = "accept/list", name = "待受理")
    @RequiresPermissions(API + "review/list")
    @Menu("事件管理")
    public APIResult reList(@Validated WrokPageDto dto) throws ByteException {
        return APIResult.success().setValue(service.acceptList(dto));
    }

    @GetMapping(value = "history/list", name = "我的已办")
    @RequiresPermissions(API + "history/list")
    @Menu("案卷列表")
    public APIResult historyList(@Validated WrokPageDto dto) {
        return APIResult.success().setValue(service.historyList(dto));
    }

    @GetMapping(value = "alarm/list", name = "告警列表")
    public APIResult alarmList() {
        return APIResult.success().setValue(service.alarmList());
    }


    @GetMapping(value = "home/list",name = "首页消息提醒")
    public APIResult homeList(){
        return APIResult.success().setValue(service.homeList());
    }

    @GetMapping(value = "gps/unit",name = "通过经纬度获取街区信息")
    public APIResult gpsUnit(@RequestParam String latitude,@RequestParam String longitude) throws ByteException {
        return APIResult.success().setValue(service.gpsUnit(latitude,longitude));
    }

    @PostMapping(value = "upd/image",name = "修改处置前 图片/视屏")
    @RequiresPermissions(API + "upd/image")
    public APIResult updImage(@Validated @RequestBody UpdImageDto dto) throws ByteException {
        service.updImage(dto);
        return APIResult.success();
    }
}

