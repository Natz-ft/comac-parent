package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/24 21:57
 * @description 告警dao
 */

@Mapper
public interface AlarmDao {
    /**
     * 按条件查询告警信息
     *
     * @param status    状态
     * @param target    类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 告警信息
     */
    List<Alarm> queryAlarmList(@Param("status") Integer status, @Param("target") String target, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 根据id查询告警详情
     *
     * @param alarmId 告警id
     * @return 告警详情
     */
    Alarm queryAlarmByAlarmId(@Param("alarmId") Long alarmId);

    /**
     * 关闭某个告警
     *
     * @param alarmId 告警id
     * @param status  状态
     */
    void updateAlarmStatusById(@Param("alarmId") Long alarmId, @Param("status") Integer status, @Param("username") String username);

    /**
     * 新增告警
     *
     * @param alarm 告警
     */
    void addAlarm(@Param("alarm") Alarm alarm);

    /**
     * 删除告警过期数据
     *
     * @param timestamp 时间戳
     */
    void deleteAlarmByTimestamp(@Param("timestamp") long timestamp);

    /**
     * 根据告警类型和状态查询告警
     *
     * @param error 异常
     * @return 告警
     */
    Alarm queryAlarmByAlarmTypeAndAlarmStatus(@Param("error") Integer error);

    /**
     * 备份告警过期数据
     *
     * @param timestamp 时间戳
     */
    void copyAlarmByTimestamp(@Param("timestamp") long timestamp);
}
