package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.CodeEnum;
import com.cmsr.comac.commander.common.Director;
import com.cmsr.comac.commander.common.ResponseData;
import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.service.ActionHistoryService;
import com.cmsr.comac.commander.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @author ztw
 * @data 2019/9/24 17:59
 * @description 告警controller
 */

@Slf4j
@RestController
@RequestMapping("/comac-commander/v2.0/alarm")
public class AlarmController {
    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 告警业务
     */
    @Autowired
    private AlarmService alarmService;
    /**
     * 用户操作历史业务
     */
    @Autowired
    private ActionHistoryService actionHistoryService;

    /**
     * 查询所有告警信息
     *
     * @param pageNum  页数
     * @param pageSize 每页大小
     * @param status   状态
     * @param target   类型
     * @param request  请求
     * @return 所有告警信息
     */
    @ApiOperation(value = "查询所有告警信息", notes = "查询所有告警信息")
    @GetMapping
    public ResponseData queryAlarmList(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @RequestParam(value = "status", required = false) Integer status,
                                       @RequestParam(value = "target", required = false) String target,
                                       @RequestParam(value = "startTime", required = false) Long startTime,
                                       @RequestParam(value = "endTime", required = false) Long endTime,
                                       HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询告警列表
        ResultInfo queryAlarmListResult = alarmService.queryAlarmList(pageNum, pageSize, status, target, startTime, endTime);
        //异常
        if (!queryAlarmListResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryAlarmListResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, queryAlarmListResult.getData());
    }

    /**
     * 查询告警详情
     *
     * @param id      告警id
     * @param request 请求
     * @return 告警详情
     */
    @ApiOperation(value = "查询告警详情", notes = "根据id查询告警详情")
    @GetMapping("/{id}")
    public ResponseData queryAlarmByAlarmId(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询告警信息
        ResultInfo alarmByAlarmIdResult = alarmService.queryAlarmByAlarmId(id);
        //异常
        if (!alarmByAlarmIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, alarmByAlarmIdResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, alarmByAlarmIdResult.getData());
    }

    /**
     * 返回某个告警关联的原始图片
     *
     * @param id       告警id
     * @param response 响应
     * @param request  请求
     * @return 告警关联的原始图片
     */
    @ApiOperation(value = "返回某个告警关联的原始图片", notes = "根据id返回某个告警关联的原始图片")
    @GetMapping("/{id}/raw-image")
    public ResponseData getAlarmImage(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询告警信息
        ResultInfo alarmByAlarmIdResult = alarmService.queryAlarmByAlarmId(id);
        //异常
        if (!alarmByAlarmIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, alarmByAlarmIdResult.getMsg());
        }
        //获取告警对象
        Alarm alarm = (Alarm) alarmByAlarmIdResult.getData();
        //获取告警所关联的图片流
        ResultInfo alarmConnectImageResult = alarmService.getAlarmConnectImage(alarm);
        //异常
        if (!alarmConnectImageResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, alarmConnectImageResult.getMsg());
        }
        //获取图片流数据
        byte[] alarmImageByte = (byte[]) alarmConnectImageResult.getData();
        //响应
        response.setContentType("image/jpeg");
        response.getOutputStream().write(alarmImageByte);
        return null;
    }

    /**
     * 关闭某个告警状态
     *
     * @param id        告警id
     * @param statusMap 状态信息
     * @param request   请求
     * @return 关闭某个告警状态
     */
    @ApiOperation(value = "关闭某个告警状态", notes = "根据id关闭某个告警状态")
    @PostMapping("/{id}/action")
    @Director
    public ResponseData updateAlarmStatusById(@PathVariable Long id, @RequestBody Map<String, Integer> statusMap, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        HttpSession session = request.getSession();
        User userSession = (User) session.getAttribute("user");
        String username = userSession.getUsername();
        ResultInfo updateAlarmStatusToOffResult = alarmService.updateAlarmStatusToOff(id, statusMap, username);
        if (!updateAlarmStatusToOffResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, updateAlarmStatusToOffResult.getMsg());
        }
        //记录操作
        try {
            actionHistoryService.addActionHistory(username, "关闭" + id + "号告警");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增访问记录失败!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, updateAlarmStatusToOffResult.getMsg());
    }
}
