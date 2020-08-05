package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.Admin;
import com.cmsr.comac.commander.common.CodeEnum;
import com.cmsr.comac.commander.common.ResponseData;
import com.cmsr.comac.commander.service.rest.CameraRestService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/12 16:14
 * @description 相机管理controller(暂不实现)
 */

@RestController
@RequestMapping("/comac-commander/v2.0/camera")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CameraController {

    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;

    /**
     * 相机接口业务
     */
    @Autowired
    private CameraRestService cameraRestService;

    /**
     * 返回具体某个相机的当前曝光值
     *
     * @param id      相机id
     * @param request 请求
     * @return 当前曝光值
     */
    @ApiOperation(value = "返回具体某个相机的当前曝光值", notes = "根据id返回具体某个相机的当前曝光值")
    @GetMapping("/{id}/exposure")
    public ResponseData getCameraExposure(@PathVariable("id") Integer id, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //调用C#接口
//        ResultInfo cameraExposureResult = cameraRestService.getCameraExposure(id);
        //异常
//        if (!cameraExposureResult.isFlag()) {
//            return ResponseData.out(CodeEnum.ERROR, cameraExposureResult.getMsg());
//        }
        HashMap<String, Float> map = new HashMap<>(1);
        map.put("value", 12.34f);
//        return ResponseData.out(CodeEnum.SUCCESSS, cameraExposureResult.getData());
        return ResponseData.out(CodeEnum.SUCCESSS, map);
    }

    /**
     * 修改具体某个相机的当前曝光值
     *
     * @param id             相机id
     * @param request        请求
     * @param cameraExposure 曝光值
     * @return 当前曝光值
     */
    @ApiOperation(value = "修改具体某个相机的当前曝光值", notes = "根据id修改具体某个相机的当前曝光值")
    @PutMapping("/{id}/exposure")
    @Admin
    public ResponseData updateCameraExposure(@PathVariable("id") Integer id, HttpServletRequest request, Map<String, Float> cameraExposure) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //调用C#接口
//        ResultInfo updateCameraExposureResult = cameraRestService.updateCameraExposure(id, cameraExposure);
        //异常
//        if (!updateCameraExposureResult.isFlag()) {
//            return ResponseData.out(CodeEnum.ERROR, updateCameraExposureResult.getMsg());
//        }
//        return ResponseData.out(CodeEnum.SUCCESSS, updateCameraExposureResult.getMsg());
        Float value = cameraExposure.get("value");
        if (null == value) {
            return ResponseData.out(CodeEnum.ERROR, "修改失败！");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, "修改成功！");
    }

}
