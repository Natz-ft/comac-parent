package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.*;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.service.ActionHistoryService;
import com.cmsr.comac.commander.service.ThermometerDataHistoryService;
import com.cmsr.comac.commander.service.ThermometerService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 14:37
 * @description 温度仪controller
 */

@RestController
@RequestMapping("/comac-commander/v2.0/thermometer")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ThermometerController {
    /**
     * 视觉图片,TXT存放路径
     */
    @Value("${BASE_DIR}")
    private String baseDir;

    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 温度仪业务
     */
    @Autowired
    private ThermometerService thermometerService;
    /**
     * 温度仪历史数据业务
     */
    @Autowired
    private ThermometerDataHistoryService thermometerDataHistoryService;
    /**
     * 用户操作历史业务
     */
    @Autowired
    private ActionHistoryService actionHistoryService;

    /**
     * 数字温度仪表盘列表
     *
     * @param location 实验室
     * @param request  请求
     * @return 数字温度仪表盘列表
     */
    @ApiOperation(value = "数字温度仪表盘列表", notes = "通过配置查询数字温度仪表盘列表")
    @GetMapping
    public ResponseData getThermometerList(@RequestParam(value = "location", required = false) String location, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询数字温度显示仪列表
        ResultInfo queryThermometerListResult = thermometerService.queryThermometerList(location);
        //异常
        if (!queryThermometerListResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryThermometerListResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, queryThermometerListResult.getData());
    }

    /**
     * 返回某个数字温度仪表盘的原始图片
     *
     * @param id       温度仪id
     * @param request  请求
     * @param response 响应
     * @return 某个数字温度仪表盘的原始图片
     * @throws Exception 异常
     */
    @ApiOperation(value = "返回某个数字温度仪表盘的原始图片", notes = "根据id返回某个数字温度仪表盘的原始图片")
    @GetMapping("/{id}/raw-image")
    public ResponseData getThermometerImage(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }

        ResultInfo queryThermometerByIdResult = thermometerService.queryThermometerById(id);
        //异常
        if (!queryThermometerByIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryThermometerByIdResult.getMsg());
        }

        //获取图片流数据
        byte[] bytes = (byte[]) queryThermometerByIdResult.getData();
        //响应
        response.setContentType("image/jpeg");
        response.getOutputStream().write(bytes);
        return null;
    }

    /**
     * 返回某个数字温度仪表盘的仪表盘读数
     *
     * @param id      温度仪id
     * @param request 请求
     * @return 某个数字温度仪表盘的仪表盘读数
     */
    @ApiOperation(value = "返回某个数字温度仪表盘的仪表盘读数", notes = "根据id返回某个数字温度仪表盘的仪表盘读数")
    @GetMapping("/{id}/data")
    public ResponseData getThermometerData(@PathVariable int id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //获取显示仪文档数据
        ResultInfo thermometerTxtDataResult = thermometerService.getThermometerTxtData(id);
        //异常
        if (!thermometerTxtDataResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, thermometerTxtDataResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, thermometerTxtDataResult.getData());
    }

    /**
     * 开启、关闭某个数字温度仪表盘的采集
     *
     * @param id        温度仪id
     * @param actionMap 功能map
     * @param request   请求
     * @return 开关
     */
    @ApiOperation(value = "关闭某个数字温度仪表盘的采集", notes = "根据id关闭某个数字温度仪表盘的采集")
    @PostMapping("/{id}/action")
    @Director
    public ResponseData updateThermometerEnableOff(@PathVariable("id") Integer id, @RequestBody Map<String, String> actionMap, HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //开启、关闭显示仪功能
        ResultInfo thermometerEnableOrEngineRecognitionEnableToOffResult = thermometerService.updateThermometerEnableOrEngineRecognitionEnableToOff(id, actionMap);

        //异常
        if (!thermometerEnableOrEngineRecognitionEnableToOffResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, thermometerEnableOrEngineRecognitionEnableToOffResult.getMsg());
        }
        //记录操作
        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("user");
            String action = actionMap.get("action");
            if (ActionEnum.DISABLE.getAction().equals(action)) {
                actionHistoryService.addActionHistory(userSession.getUsername(), "关闭" + id + "号数字温度显示仪采集");
            }
            if (ActionEnum.RECOGNITION_DISABLE.getAction().equals(action)) {
                actionHistoryService.addActionHistory(userSession.getUsername(), "关闭" + id + "号数字温度显示仪视觉识别");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, thermometerEnableOrEngineRecognitionEnableToOffResult.getMsg());
    }

    /**
     * 返回数字仪表盘数据的历史记录
     *
     * @param id        温度仪id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param request   请求
     * @return 数字仪表盘数据的历史记录
     */
    @ApiOperation(value = "返回数字仪表盘数据的历史记录", notes = "根据id返回某个数字仪表盘数据的历史记录")
    @GetMapping("/{id}/history")
    public ResponseData getThermometerHistory(@PathVariable("id") Long id, @RequestParam(value = "startTime", required = false) Long startTime, @RequestParam(value = "endTime", required = false) Long endTime, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //获取数字仪表盘数据的历史记录
        ResultInfo thermometerDataHistoryByIdAndTimestampResult = thermometerDataHistoryService.queryThermometerDataHistoryByIdAndTimestamp(id, startTime, endTime);
        //异常
        if (!thermometerDataHistoryByIdAndTimestampResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, thermometerDataHistoryByIdAndTimestampResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, thermometerDataHistoryByIdAndTimestampResult.getData());
    }

    /**
     * @param jsonObject 相机坐标
     * @return com.cmsr.comac.commander.common.ResponseData
     * @Description 更改相机坐标
     */

    @GetMapping("/updateCameraOcr")
    public ResponseData UpdateThermometerCameraOcr(@RequestBody JSONObject jsonObject) {
        ResultInfo resultInfo = null;
        try {
            resultInfo = thermometerService.UpdateThermometerCameraOcr(jsonObject);
        } catch (JSONException e) {
            log.info("相机坐标更改异常");
        }
        if (resultInfo != null && resultInfo.isFlag() == true) {
            return ResponseData.out(CodeEnum.SUCCESSS, resultInfo);
        }
        return ResponseData.out(CodeEnum.ERROR, "相机坐标更改失败");
    }

}
