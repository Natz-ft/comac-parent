package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.ThermometerDataHistory;

import java.util.List;


/**
 * @author ztw
 * @data 2019/9/29 17:58
 * @description 温度仪历史数据接口
 */

public interface ThermometerDataHistoryService {
    /**
     * 返回数字仪表盘数据的历史记录
     *
     * @param id        温度仪id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 数字仪表盘数据的历史记录
     */
    ResultInfo queryThermometerDataHistoryByIdAndTimestamp(Long id, Long startTime, Long endTime);

    /**
     * 新增数字温度显示仪的仪表盘数据
     *
     * @param thermometerDataHistory 数字温度显示仪的仪表盘数据
     */
    void addThermometerDataHistory(ThermometerDataHistory thermometerDataHistory);

    /**
     * 过期仪表盘数值数据
     *
     * @param timestamp 时间戳
     */
    void deleteThermometerDataHistory(long timestamp);

    /**
     * 温度仪数据处理
     *
     * @param id              温度仪id
     * @param timestamp       时间戳
     * @param fileName        文件名
     * @param thermometerData 温度仪数据
     * @throws Exception 异常
     */
    void thermometerDataProcess(Integer id, long timestamp, String fileName, ThermometerDataHistory thermometerData,byte[] bytes) throws Exception;

    /**
     * 温度仪温度数据异常
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param error     异常
     */
    void addThermometerDataVisionAlarm(Integer id, long timestamp, Integer error);
    /**
     * 备份过期仪表盘数值数据
     *
     * @param timestamp 时间戳
     */
    void copyThermometerDataHistory(long timestamp);
}
