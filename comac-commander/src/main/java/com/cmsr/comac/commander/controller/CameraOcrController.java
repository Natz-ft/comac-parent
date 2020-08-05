package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.Admin;
import com.cmsr.comac.commander.common.CodeEnum;
import com.cmsr.comac.commander.common.ResponseData;
import com.cmsr.comac.commander.pojo.CameraOcrCoord;
import com.cmsr.comac.commander.service.rest.CameraRestService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

/**
 * @author ztw
 * @data 2019/10/12 17:27
 * @description 相机OCR
 */

@RestController
@RequestMapping("/comac-commander/v2.0/ocr/camera")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CameraOcrController {

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
     * 设置某个逻辑相机的需要做ORC的坐标
     *
     * @param id             相机id
     * @param cameraOcrCoord 相机OCR坐标
     * @param request        请求
     * @return ORC的坐标
     */
    @ApiOperation(value = "设置某个逻辑相机的需要做OCR的坐标", notes = "根据id设置某个逻辑相机的需要做OCR的坐标")
    @PostMapping("/{id}")
    @Admin
    public ResponseData setCameraCoord(@PathVariable("id") Integer id, @RequestBody CameraOcrCoord cameraOcrCoord, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //调用C#接口
//        ResultInfo cameraOcrCoordinateResult = cameraRestService.setCameraOcrCoordinate(id, cameraOcrCoord);
        //异常
//        if (!cameraOcrCoordinateResult.isFlag()) {
//            return ResponseData.out(CodeEnum.ERROR, cameraOcrCoordinateResult.getMsg());
//        }
//        return ResponseData.out(CodeEnum.SUCCESSS, cameraOcrCoordinateResult.getMsg());
        if (null == cameraOcrCoord) {
            return ResponseData.out(CodeEnum.ERROR, "设置失败!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, "设置成功!");
    }
}
