package com.cmsr.comac.commander.service.impl.rest;


import com.cmsr.comac.commander.pojo.CameraOcrCoord;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.ThermometerDataHistory;
import com.cmsr.comac.commander.service.rest.CameraRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/15 10:26
 * @description
 */

@Service
@Slf4j
public class CameraRestServiceImpl implements CameraRestService {

    /**
     * GET 触发拍摄动作，返回具体某个相机的当前画面
     */
    @Value("${getCameraImage}")
    private String getCameraImage;

    /**
     * GET 返回具体某个相机的当前曝光值
     */
    @Value("${getCameraExposure}")
    private String getCameraExposure;

    /**
     * PUT 修改具体某个相机的当前曝光值
     */
    @Value("${putCameraExposure}")
    private String putCameraExposure;

    /**
     * POST 设置某个逻辑相机的需要做ORC的坐标
     */
    @Value("${postCameraOcrCoordinate}")
    private String postCameraOcrCoordinate;

    /**
     * POST 识别图片中的字符，提交数字温度仪图片，返回结果
     */
    @Value("${postOcrResult}")
    private String postOcrResult;

    /**
     * POST 提交桌面图片，识别图片中的颜色，返回结果
     */
    @Value("${postOcrScrResult}")
    private String postOcrScrResult;


    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;

    /**
     * 触发拍摄动作，返回具体某个相机的当前画面
     *
     * @param cameraId 相机id
     * @return 具体某个相机的当前画面
     */
    @Override
    public ResultInfo getCameraImage(Integer cameraId) {
        //把token设置到头信息
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        //调用C#获取指定逻辑相机所拍摄的图片
        try {
            ResponseEntity<byte[]> cameraData = restTemplate.exchange(getCameraImage, HttpMethod.GET, httpEntity, byte[].class, cameraId);
            byte[] bytes = cameraData.getBody();
            return new ResultInfo(true, bytes);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "获取相机图片失败!");
        }
    }

    /**
     * 返回具体某个相机的当前曝光值
     *
     * @param cameraId 相机id
     * @return 具体某个相机的当前曝光值
     */
    @Override
    public ResultInfo getCameraExposure(Integer cameraId) {
        //把token设置到头信息
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        //调用C#接口,获取相机曝光值
        try {
            ResponseEntity<Float> cameraData = restTemplate.exchange(getCameraExposure, HttpMethod.GET, httpEntity, Float.class, cameraId);
            Float cameraExposure = cameraData.getBody();
            //封装结果
            HashMap<String, Float> cameraExposureMap = new HashMap<>(1);
            cameraExposureMap.put("value", cameraExposure);
            return new ResultInfo(true, cameraExposureMap);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "获取相机曝光值异常!");
        }
    }

    /**
     * 修改具体某个相机的当前曝光值
     *
     * @param cameraId       相机id
     * @param cameraExposure 曝光
     * @return 具体某个相机的当前曝光值
     */
    @Override
    public ResultInfo updateCameraExposure(Integer cameraId, Map<String, Float> cameraExposure) {
        //获取修改的曝光值
        Float exposureValue = cameraExposure.get("value");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //把token设置到头信息
        headers.add("x-auth-token", screenToken);
        //设置曝光值和头信息
        HttpEntity<Object> httpEntity = new HttpEntity<>(exposureValue, headers);
        try {
            ResponseEntity<Boolean> cameraData = restTemplate.exchange(putCameraExposure, HttpMethod.PUT, httpEntity, Boolean.class, cameraId);
            if (cameraData.getBody()) {
                return new ResultInfo(true, "修改曝光值成功!");
            }
            return new ResultInfo(false, "修改曝光值失败!");
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "修改曝光值失败!");
        }
    }

    /**
     * 设置某个逻辑相机的需要做ORC的坐标
     *
     * @param cameraId       相机id
     * @param cameraOcrCoord ORC的坐标
     * @return ORC的坐标
     */
    @Override
    public ResultInfo setCameraOcrCoordinate(Integer cameraId, CameraOcrCoord cameraOcrCoord) {
        //把token设置到头信息
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(cameraOcrCoord, headers);

        try {
            ResponseEntity<Boolean> isCameraOcr = restTemplate.exchange(postCameraOcrCoordinate, HttpMethod.POST, httpEntity, Boolean.class, cameraId);
            if (isCameraOcr.getBody()) {
                return new ResultInfo(true, "相机OCR坐标设置成功!");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "相机OCR坐标设置失败!");
        }
        return new ResultInfo(false, "相机OCR坐标设置失败!");
    }


    /**
     * 提交数字温度仪图片，识别图片中的字符，返回结果
     *
     * @param imageBytes 图片流
     * @return result
     */
    @Override
    public ResultInfo getCameraOcrResult(byte[] imageBytes) {
        //设置token
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        //调用C#传入图片流获取OCR识别结果
        HttpEntity<Object> httpEntity = new HttpEntity<>(imageBytes, headers);
        //调用C#接口获取温度仪数据
        try {
            ResponseEntity<ThermometerDataHistory> thermometerDataResult = restTemplate.exchange(postOcrResult, HttpMethod.POST, httpEntity, ThermometerDataHistory.class);
            ThermometerDataHistory thermometerData = thermometerDataResult.getBody();
            return new ResultInfo(true, thermometerData);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "获取识别结果失败!");
        }
    }

    /**
     * 提交桌面图片，识别图片中的颜色，返回结果
     *
     * @param screenBytes 截屏图片流
     * @return result
     */
    @Override
    public ResultInfo getScreenOcrResult(byte[] screenBytes) {
        //设置token
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        //调用C#传入图片流获取OCR识别结果
        HttpEntity<Object> httpEntity = new HttpEntity<>(screenBytes, headers);
        //调用C#接口获取温度仪数据
        try {
            ResponseEntity<Integer> screenOcrColorData = restTemplate.exchange(postOcrScrResult, HttpMethod.POST, httpEntity, Integer.class);
            Integer ocrColorDataBody = screenOcrColorData.getBody();
            return new ResultInfo(true, ocrColorDataBody);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用C#接口异常!");
            return new ResultInfo(false, "获取识别结果失败!");
        }
    }
}
