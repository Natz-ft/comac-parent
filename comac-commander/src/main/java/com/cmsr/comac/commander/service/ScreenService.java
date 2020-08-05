package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.Screen;


import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 10:16
 * @description 截屏接口
 */

public interface ScreenService {

    /**
     * 查询截屏列表
     *
     * @param location 实验室
     * @return 截屏列表
     */
    ResultInfo queryScreenList(String location);

    /**
     * 根据id查询截屏对象
     *
     * @param id 截屏id
     * @return 截屏
     * @throws Exception 异常
     */
    ResultInfo queryScreenById(Integer id) throws Exception;

    /**
     * 压缩图片
     *
     * @param bytes 图片流
     * @return 截屏图片
     */
    ResultInfo compressImage(byte[] bytes);

    /**
     * 关闭屏幕功能
     *
     * @param id        截屏id
     * @param actionMap map
     * @return result
     */
    ResultInfo updateScreenEnableOrRecognitionEnableToOff(Integer id, Map<String, String> actionMap);

    /**
     * 获取截屏图片
     *
     * @param screen 截屏
     * @return 截屏
     */
    ResultInfo getScreenImage(Screen screen);

    /**
     * 新增截屏告警
     *
     * @param id        截屏id
     * @param timestamp 时间戳
     * @param fileName  文件名
     * @param alarmMap  告警
     */
    void addScreenAlarm(Integer id, long timestamp, String fileName, Map<String, String> alarmMap,byte[] bytes);

    /**
     * 截屏获取不到图片时告警
     *
     * @param id        截屏id
     * @param timestamp 时间戳
     * @param errorType 告警
     */
    void getScreenImageAlarm(Integer id, long timestamp, Integer errorType);
}
