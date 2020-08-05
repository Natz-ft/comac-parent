package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.ResultInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @author ztw
 * @data 2019/9/24 20:49
 * @description 告警接口
 */

public interface AlarmService {

    /**
     * 按条件查询所有告警信息
     *
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @param status    状态
     * @param target    类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 所有告警信息
     */
    ResultInfo queryAlarmList(Integer pageNum, Integer pageSize, Integer status, String target, Long startTime, Long endTime);

    /**
     * 根据id查询告警详情
     *
     * @param alarmId 告警id
     * @return 告警详情
     */
    ResultInfo queryAlarmByAlarmId(Long alarmId);

    /**
     * 关闭某个告警
     *
     * @param id        告警id
     * @param statusMap 状态map
     * @return result
     */
    ResultInfo updateAlarmStatusToOff(Long id, Map<String, Integer> statusMap, String username);

    /**
     * 新增告警
     *
     * @param alarm 告警
     */
    void addAlarm(Alarm alarm);

    /**
     * 获取某个告警关联的原始图片
     *
     * @param alarm 告警
     * @return 告警关联的原始图片
     * @throws Exception 异常
     */
    ResultInfo getAlarmConnectImage(Alarm alarm) throws Exception;


    /**
     * 删除告警过期数据
     *
     * @param timestamp 时间戳
     */
    void deleteAlarmByTimestamp(long timestamp);   /**
     * 备份告警过期数据
     *
     * @param timestamp 时间戳
     */
    void copyAlarmByTimestamp(long timestamp);
}
