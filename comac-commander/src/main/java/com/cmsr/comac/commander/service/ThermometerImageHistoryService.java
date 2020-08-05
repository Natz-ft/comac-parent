package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ThermometerImageHistory;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/10 15:25
 * @description 温度仪图片历史数据接口
 */

public interface ThermometerImageHistoryService {
    /**
     * 新增数字温度显示仪图片历史数据
     *
     * @param thermometerImageHistory 数字温度显示仪图片历史数据
     */
    void addThermometerImageHistory(ThermometerImageHistory thermometerImageHistory);

    /**
     * 过期数字温度显示仪数据
     *
     * @param timestamp 时间戳
     * @return 过期数字温度显示仪数据
     */
    List<ThermometerImageHistory> queryThermometerImageHistory(long timestamp);

    /**
     * 清理周期之外的数字温度显示仪数据
     *
     * @param timestamp 时间戳
     */
    void deleteThermometerImageHistory(long timestamp);

    /**
     * 数字温度显示仪图片数据异常告警
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param fileName  文件名
     * @param error     异常
     */
    void addThermometerImageHistoryAlarm(Integer id, long timestamp, String fileName, Integer error);

    /**
     * 温度仪视觉文件告警
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param error     异常
     */
    void addThermometerVisionAlarm(Integer id, long timestamp, Integer error);

    /**
     * 备份周期之外的数字温度显示仪数据
     *
     * @param timestamp 时间戳
     */
    void copyThermometerImageHistory(long timestamp);
}
