package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.AlarmTargetEnum;
import com.cmsr.comac.commander.dao.AlarmDao;
import com.cmsr.comac.commander.dao.ThermometerImageHistoryDao;
import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.ThermometerImageHistory;
import com.cmsr.comac.commander.service.ThermometerImageHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author ztw
 * @data 2019/10/10 15:26
 * @description 温度仪图片历史数据实现
 */

@Service
@Slf4j
public class ThermometerImageHistoryServiceImpl implements ThermometerImageHistoryService {
    /**
     * 温度仪图片历史数据dao
     */
    @Autowired
    private ThermometerImageHistoryDao thermometerImageHistoryDao;
    /**
     * 告警dao
     */
    @Autowired
    private AlarmDao alarmDao;

    /**
     * 新增数字温度显示仪图片历史数据
     *
     * @param thermometerImageHistory 数字温度显示仪图片历史数据
     */
    @Override
    public void addThermometerImageHistory(ThermometerImageHistory thermometerImageHistory) {
        thermometerImageHistoryDao.addThermometerImageHistory(thermometerImageHistory);

    }

    /**
     * 过期数字温度显示仪数据
     *
     * @param timestamp 时间戳
     * @return 过期数字温度显示仪数据
     */
    @Override
    public List<ThermometerImageHistory> queryThermometerImageHistory(long timestamp) {
        return thermometerImageHistoryDao.queryThermometerImageHistory(timestamp);
    }

    /**
     * 清理周期之外的数字温度显示仪数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void deleteThermometerImageHistory(long timestamp) {
        thermometerImageHistoryDao.deleteThermometerImageHistory(timestamp);
    }

    /**
     * 数字温度显示仪图片数据异常告警
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param fileName  文件名
     * @param error     异常
     */
    @Override
    public void addThermometerImageHistoryAlarm(Integer id, long timestamp, String fileName, Integer error) {
        log.info(id + "号温度显示仪图片数据异常!");
        //先查询告警列表是否已存在告警中,并且告警类型为error的数据
        Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(error);
        //如果没有,则插入
        if (null == queryAlarm) {
            //写入告警记录表
            Alarm alarm = new Alarm();
            //告警目标
            alarm.setAlarmTarget(AlarmTargetEnum.THERMOMETER.getAlarmTarget());
            //告警目标的id
            alarm.setAlarmTargetId(id);
            //告警中
            alarm.setAlarmStatus(1);
            //告警触发时间,默认为获取到图时的时间
            alarm.setAlarmTime(timestamp);
            //告警类型
            alarm.setAlarmType(error);
            //触发告警的图片文件名
            alarm.setAlarmPicName(fileName);
            alarmDao.addAlarm(alarm);
        }
    }

    /**
     * 温度仪视觉文件告警
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param error     异常
     */
    @Override
    public void addThermometerVisionAlarm(Integer id, long timestamp, Integer error) {
        log.info(id + "号温度显示仪视觉数据存在异常!");
        //先查询告警列表是否已存在告警中,并且告警类型为error的数据
        Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(error);
        //如果没有,则插入
        if (null == queryAlarm) {
            //写入告警记录表
            Alarm alarm = new Alarm();
            //告警目标
            alarm.setAlarmTarget(AlarmTargetEnum.VISION.getAlarmTarget());
            //告警目标的id
            alarm.setAlarmTargetId(id);
            //告警中
            alarm.setAlarmStatus(1);
            //告警触发时间,默认为获取到图时的时间
            alarm.setAlarmTime(timestamp);
            //告警类型
            alarm.setAlarmType(error);
            alarmDao.addAlarm(alarm);
        }
    }

    @Override
    public void copyThermometerImageHistory(long timestamp) {
        thermometerImageHistoryDao.copyThermometerImageHistory(timestamp);
    }
}
