package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.*;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.service.ActionHistoryService;
import com.cmsr.comac.commander.service.ScreenService;
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
 * @data 2019/9/29 10:48
 * @description 截屏controller
 */

@RestController
@RequestMapping("/comac-commander/v2.0/screen")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ScreenController {
    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;

    /**
     * 截屏业务
     */
    @Autowired
    private ScreenService screenService;

    /**
     * 操作历史业务
     */
    @Autowired
    private ActionHistoryService actionHistoryService;


    /**
     * 获取截屏列表
     *
     * @param location 实验室
     * @return 截屏列表
     */
    @ApiOperation(value = "获取屏幕列表", notes = "通过配置文件获取列表")
    @GetMapping
    public ResponseData getScreenList(@RequestParam(value = "location", required = false) String location, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        ResultInfo screenListResult = screenService.queryScreenList(location);

        if (!screenListResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, screenListResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, screenListResult.getData());
    }

    /**
     * 获取截屏图片
     *
     * @param id       截屏id
     * @param response 响应
     * @return 截屏图片
     * @throws Exception 异常
     */
    @ApiOperation(value = "获取具体某个屏幕的原始图⽚", notes = "根据id获取指定的电脑屏幕图片")
    @GetMapping("/{id}/raw-image")
    public ResponseData getScreen(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        //获取截屏对象
        ResultInfo queryScreenByIdResult = screenService.queryScreenById(id);
        if (!queryScreenByIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryScreenByIdResult.getMsg());
        }
        //获取图片流
        byte[] bytes = (byte[]) queryScreenByIdResult.getData();
        //响应截图
        response.setContentType("image/jpeg");
        response.getOutputStream().write(bytes);
        return null;
    }

    /**
     * 返回具体某个屏幕的缩略图
     *
     * @param id       屏幕id
     * @param response 响应
     * @return 屏幕的缩略图
     * @throws Exception 异常
     */
    @ApiOperation(value = "返回具体某个屏幕的缩略图", notes = "根据id返回指定的电脑屏幕缩略图片")
    @GetMapping("/{id}/thumbnail")
    public ResponseData getThumbnail(@PathVariable Integer id, HttpServletResponse response) throws Exception {

        //获取截屏对象
        ResultInfo queryScreenByIdResult = screenService.queryScreenById(id);

        if (!queryScreenByIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryScreenByIdResult.getMsg());
        }
        //获取图片流
        byte[] bytes = (byte[]) queryScreenByIdResult.getData();

        //压缩图片
        ResultInfo compressImageResult = screenService.compressImage(bytes);
        //异常
        if (!compressImageResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, compressImageResult.getMsg());
        }

        //获取压缩后的图片流
        byte[] compressImageByteData = (byte[]) compressImageResult.getData();

        //响应图片数据
        response.setContentType("image/jpeg");
        response.getOutputStream().write(compressImageByteData);
        return null;
    }

    /**
     * 开/关屏幕功能
     *
     * @param id        屏幕id
     * @param actionMap 功能map
     * @param request   请求
     * @return 开关
     */
    @ApiOperation(value = "关闭屏幕功能", notes = "根据id获取指定的电脑屏幕缩略图片")
    @PostMapping("/{id}/action")
    @Director
    public ResponseData updateScreenEnableOff(@PathVariable("id") Integer id, @RequestBody Map<String, String> actionMap, HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询截屏
        ResultInfo queryScreenByIdResult = screenService.queryScreenById(id);
        //异常
        if (!queryScreenByIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryScreenByIdResult.getMsg());
        }
        //关闭截屏
        ResultInfo updateScreenEnableOrRecognitionEnableResult = screenService.updateScreenEnableOrRecognitionEnableToOff(id, actionMap);
        //异常
        if (!updateScreenEnableOrRecognitionEnableResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, updateScreenEnableOrRecognitionEnableResult.getMsg());
        }

        //记录操作
        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("user");
            String action = actionMap.get("action");
            if (ExperimentActionEnum.DISABLE.getAction().equals(action)) {
                actionHistoryService.addActionHistory(userSession.getUsername(), "关闭" + id + "号屏幕采集");
            }
            if (ExperimentActionEnum.RECOGNITION_DISABLE.getAction().equals(action)) {
                actionHistoryService.addActionHistory(userSession.getUsername(), "关闭" + id + "号屏幕视觉识别");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, updateScreenEnableOrRecognitionEnableResult.getMsg());
    }
}
