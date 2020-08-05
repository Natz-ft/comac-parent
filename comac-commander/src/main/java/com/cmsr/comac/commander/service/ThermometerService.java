package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 14:38
 * @description 温度仪接口
 */

public interface ThermometerService {
    /**
     * 数字温度仪表盘列表
     *
     * @param location 实验室
     * @return 数字温度仪表盘列表
     */
    ResultInfo queryThermometerList(String location);

    /**
     * 获取某个数字温度仪表盘的原始图片
     *
     * @param id 温度仪id
     * @return 某个数字温度仪表盘的原始图片
     * @throws Exception 异常
     */
    ResultInfo queryThermometerById(Integer id) throws Exception;

    /**
     * 获取数字温度显示仪文档数据
     *
     * @param id 温度仪id
     * @return 数字温度显示仪文档数据
     */
    ResultInfo getThermometerTxtData(Integer id) throws Exception;

    /**
     * 关闭某个数字温度仪表盘的采集
     *
     * @param id        温度仪id
     * @param actionMap map
     * @return result
     */
    ResultInfo updateThermometerEnableOrEngineRecognitionEnableToOff(Integer id, Map<String, String> actionMap);

    /**
     * @Description 更改数字温度仪坐标
     * @Param jsonObject
     * @Return
     */
    ResultInfo UpdateThermometerCameraOcr(JSONObject jsonObject) throws JSONException;

    /**
     * @param id 温度仪id
     * @return com.cmsr.comac.commander.pojo.ResultInfo
     * @Description 根据温度仪id获取温度仪数据并存储
     */
    ResultInfo getThermometerData(Integer cameraId,Integer...id);
}