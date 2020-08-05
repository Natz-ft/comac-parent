package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.CodeEnum;
import com.cmsr.comac.commander.common.Director;
import com.cmsr.comac.commander.common.ResponseData;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.service.EngineService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 10:48
 * @description 油机controller(暂不实现)
 */

@RestController
@RequestMapping("/comac-commander/v2.0/engine")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class EngineController {
    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 视觉图片,TXT存放路径
     */
    @Value("${BASE_DIR}")
    private String baseDir;

    /**
     * 最大超时时间(可在.yml文件进行配置)
     */
    @Value("${maxTimeOut}")
    private long maxTimeOut;
    /**
     * 油机业务
     */
    @Autowired
    private EngineService engineService;

    /**
     * 获取油机列表
     *
     * @param location 实验室
     * @param request  请求
     * @return 油机列表
     */
    @ApiOperation(value = "获取油机列表", notes = "通过配置文件获取油机列表")
    @GetMapping
    public ResponseData getEngineList(@RequestParam("location") String location, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        ResultInfo resultInfo = engineService.queryEngineList(location);
        if (!resultInfo.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, resultInfo.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, resultInfo.getData());
    }

    /**
     * 返回某个油机的原始图片
     *
     * @param id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
//    @ApiOperation(value = "返回某个油机的原始图片", notes = "根据id返回某个油机的原始图片")
//    @GetMapping("/{id}/raw-image")
//    public ResponseData getEngineImage(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String token = request.getHeader("x-auth-token");
//        if (!screenToken.equals(token)) {
//            log.info("无token或token错误!");
//            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
//        }
//
////        ResultInfo engineImageResult = engineService.queryEngineById(id);
////        if (!engineImageResult.isFlag()) {
////            return ResponseData.out(CodeEnum.NOT_FOUND, engineImageResult.getMsg());
////        }
//
//        //获取图片流
////        byte[] bytes = (byte[]) engineImageResult.getData();
//
//        Map<String, Object> imageFile = DataUtils.getImageFile(baseDir + "\\engine\\" + id + "\\engine.jpg", maxTimeOut);
//        if (null != imageFile.get("error")) {
//            return ResponseData.out(CodeEnum.NOT_FOUND, imageFile.get("error"));
//        }
//        byte[] bytes = (byte[]) imageFile.get("imageByteData");
//        //响应
//        response.setContentType("image/jpeg");
//        response.getOutputStream().write(bytes);
//        return null;
//    }

    /**
     * 开/关某个油机的采集
     *
     * @param id        油机id
     * @param actionMap 功能
     * @param request   请求
     * @return 开关状态
     * @throws Exception 异常
     */
    @ApiOperation(value = "关闭某个油机的采集", notes = "根据id关闭某个油机的采集")
    @PostMapping("/{id}/action")
    @Director
    public ResponseData updateEngineEnableOff(@PathVariable("id") Integer id, @RequestBody Map<String, String> actionMap, HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }

        //查询油机
        ResultInfo engineByIdResult = engineService.queryEngineById(id);
        //异常
        if (!engineByIdResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, engineByIdResult.getMsg());
        }

        //关闭油机采集
        ResultInfo engineEnableOrRecognitionEnableToOffResult = engineService.updateEngineEnableOrRecognitionEnableToOff(id, actionMap);
        //异常
        if (!engineEnableOrRecognitionEnableToOffResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, engineEnableOrRecognitionEnableToOffResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, engineEnableOrRecognitionEnableToOffResult.getMsg());

    }

}
