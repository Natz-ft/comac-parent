package com.cmsr.comac.commander.service.rest;

import com.cmsr.comac.commander.pojo.CameraOcrCoord;
import com.cmsr.comac.commander.pojo.ResultInfo;

import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/15 10:24
 * @description
 */

public interface CameraRestService {

    /**
     * 触发拍摄动作，返回具体某个相机的当前画面
     *
     * @param cameraId 相机id
     * @return 具体某个相机的当前画面
     */
    ResultInfo getCameraImage(Integer cameraId);

    /**
     * 返回具体某个相机的当前曝光值
     *
     * @param cameraId 相机ID
     * @return 某个相机的当前曝光值
     */
    ResultInfo getCameraExposure(Integer cameraId);

    /**
     * 修改具体某个相机的当前曝光值
     *
     * @param cameraId       相机id
     * @param cameraExposure 曝光
     * @return result
     */
    ResultInfo updateCameraExposure(Integer cameraId, Map<String, Float> cameraExposure);

    /**
     * 设置某个逻辑相机的需要做ORC的坐标
     *
     * @param cameraId       相机id
     * @param cameraOcrCoord OCR坐标
     * @return ORC的坐标
     */
    ResultInfo setCameraOcrCoordinate(Integer cameraId, CameraOcrCoord cameraOcrCoord);

    /**
     * 提交数字温度仪图片，识别图片中的字符，返回结果
     *
     * @param imageBytes 图片流
     * @return result
     */
    ResultInfo getCameraOcrResult(byte[] imageBytes);

    /**
     * 提交桌面图片，识别图片中的颜色，返回结果
     *
     * @param screenBytes 图片流
     * @return result
     */
    ResultInfo getScreenOcrResult(byte[] screenBytes);


}
