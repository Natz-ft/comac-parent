package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.AlarmTargetEnum;
import com.cmsr.comac.commander.dao.AlarmDao;
import com.cmsr.comac.commander.dao.ThermometerDataHistoryDao;
import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.ThermometerDataHistory;
import com.cmsr.comac.commander.service.AlarmService;
import com.cmsr.comac.commander.service.ThermometerDataHistoryService;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ztw
 * @data 2019/9/29 17:58
 * @description 温度仪历史数据实现
 */

@Service
@Slf4j
public class ThermometerDataHistoryServiceImpl implements ThermometerDataHistoryService {
    /**
     * 视觉图片保存的路径
     */
    @Value("${Vision_Base_Dir}")
    private String visionBaseDir;
    /**
     * 视觉图片,TXT存放路径
     */
    @Value("${BASE_DIR}")
    private String baseDir;
    /**
     * 查询历史温度仪数据的条数
     */
    @Value("${TimesBefore}")
    Integer times;

    /**
     * 温度仪历史数据dao
     */
    @Autowired
    private ThermometerDataHistoryDao thermometerDataHistoryDao;
    /**
     * 告警业务
     */
    @Autowired
    private AlarmService alarmService;
    /**
     * 告警dao
     */
    @Autowired
    private AlarmDao alarmDao;
    /**
     * 判断条件
     */
    private boolean flag = true;

    /**
     * 返回数字仪表盘数据的历史记录
     *
     * @param id        温度仪id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 数字仪表盘数据的历史记录
     */
    @Override
    public ResultInfo queryThermometerDataHistoryByIdAndTimestamp(Long id, Long startTime, Long endTime) {
        try {
            List<ThermometerDataHistory> thermometerDataHistoryList = thermometerDataHistoryDao.queryThermometerDataHistoryByIdAndTimestamp(id, startTime, endTime);
            if (CollectionUtils.isEmpty(thermometerDataHistoryList)) {
                log.info("未查询到数据!");
                return new ResultInfo(false, "无温度显示仪数据!");
            }
            return new ResultInfo(true, thermometerDataHistoryList);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("服务器异常!");
            return new ResultInfo(false, "服务器异常!");
        }
    }

    /**
     * 新增数字温度显示仪的仪表盘数据
     *
     * @param thermometerDataHistory 数字温度显示仪的仪表盘数据
     */
    @Override
    public void addThermometerDataHistory(ThermometerDataHistory thermometerDataHistory) {
        thermometerDataHistoryDao.addThermometerDataHistory(thermometerDataHistory);
    }

    /**
     * 清理数字温度显示仪周期之外的仪表盘数值数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void deleteThermometerDataHistory(long timestamp) {
        thermometerDataHistoryDao.deleteThermometerDataHistory(timestamp);
    }

    /**
     * 温度仪数据处理
     *
     * @param id              温度仪id
     * @param timestamp       时间戳
     * @param fileName        文件名
     * @param thermometerData 温度仪数据
     * @throws Exception 异常
     */
    @Override
    public void thermometerDataProcess(Integer id, long timestamp, String fileName, ThermometerDataHistory thermometerData, byte[] bytes
    ) throws Exception {
        //上段顶部数值
        Integer upperTop = thermometerData.getUpperTop();
        //上段中部数值
        Integer upperMiddle = thermometerData.getUpperMiddle();
        //中段顶部数值
        Integer centreTop = thermometerData.getCentreTop();
        //中段中部数值
        Integer centreMiddle = thermometerData.getCentreMiddle();
        //下段顶部数值
        Integer downTop = thermometerData.getDownTop();
        //下段中部数值
        Integer downMiddle = thermometerData.getDownMiddle();

        //将温度仪数据写入thermometer_data_history表
        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
        thermometerDataHistory.setThermometerId(id);
        //使用获取图片时的时间戳,默认图片和数据同时获取到
        thermometerDataHistory.setTimestamp(timestamp);
        //上段顶部数值(加热温度)
        thermometerDataHistory.setUpperTop(upperTop);
        //上段中部数值(设定温度)
        thermometerDataHistory.setUpperMiddle(upperMiddle);
        //中段顶部数值(加热温度)
        thermometerDataHistory.setCentreTop(centreTop);
        //中段中部数值(设定温度)
        thermometerDataHistory.setCentreMiddle(centreMiddle);
        //下段顶部数值(加热温度)
        thermometerDataHistory.setDownTop(downTop);
        //下段中部数值(设定温度)
        thermometerDataHistory.setDownMiddle(downMiddle);
        thermometerDataHistoryDao.addThermometerDataHistory(thermometerDataHistory);
        log.info(id + "号温度仪温度数据已写入数据库!");

        //TODO
        //获取温度仪历史数据id
        Long dataHistoryId = thermometerDataHistory.getId();
        if (flag) {
            //上段温度处于设定温度正负2摄氏度，视为稳定阶段,连续2次温度均稳定在设定温度的正负两度，此时才进行告警的判断
            if (Math.abs(upperTop - 900) <= 2 & Math.abs(centreTop - 900) <= 2 & Math.abs(downTop - 900) <= 2) {
                AtomicInteger atomicInteger = new AtomicInteger(0);
                //查询该条记录之前的若干条历史数据
                List<ThermometerDataHistory> thermometerDataHistories = thermometerDataHistoryDao.queryThermometerDataHistoryById(dataHistoryId, times);
                thermometerDataHistories.forEach(dataHistory -> {
                    Integer upperTop1 = dataHistory.getUpperTop();
                    Integer centreTop1 = dataHistory.getCentreTop();
                    Integer downTop1 = dataHistory.getDownTop();
                    //连续五次温度均处于设定温度正负2摄氏度后开始告警功能
                    if ((Math.abs(upperTop1 - 900) <= 2) & (Math.abs(centreTop1 - 900) <= 2) & (Math.abs(downTop1 - 900) <= 2)) {
                        atomicInteger.getAndIncrement();
                    }
                    if (atomicInteger.get() == 4) {
                        flag = false;
                    } else {
                        return;
                    }
                });
            }
        }
        //上段温度超出设定温度值
        if (upperTop > (upperMiddle + 5)) {
            log.info(id + "号数字温度显示仪上段超出设定温度范围!");
            //先查询告警列表是否已存在告警中,并且告警类型为error的数据
            Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(4001);
            //如果没有,则插入
            if (null == queryAlarm) {
                //写入告警记录表
                Alarm alarm = new Alarm();
                //告警目标
                String alarmTarget = AlarmTargetEnum.THERMOMETER.getAlarmTarget();
                alarm.setAlarmTarget(alarmTarget);
                //告警目标的id
                alarm.setAlarmTargetId(id);
                //告警中
                alarm.setAlarmStatus(1);
                //告警触发时间,默认为获取到图时的时间
                alarm.setAlarmTime(timestamp);
                //告警类型4001:上段加热温度超出设定温度范围
                alarm.setAlarmType(4001);
                //触发告警的图片文件名
                alarm.setAlarmPicName(fileName);
                alarmService.addAlarm(alarm);
                //TODO  保存温度仪告警图片

                File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget + "-alarm" + "\\" + id + "\\" + fileName);
                if (!alarmImageFile.exists()) {
                    alarmImageFile.getParentFile().mkdirs();
                }
                try {
                    Files.write(bytes, alarmImageFile);
                } catch (IOException e) {
                    log.info("id={}的屏幕告警图片写入异常", id);
                }
            }
        }

        //中段温度超出设定温度值范围
        if (centreTop > (centreMiddle + 5)) {
            log.info(id + "号数字温度显示仪中段超出设定温度范围!");
            //先查询告警列表是否已存在告警中,并且告警类型为error的数据
            Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(4002);
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
                //告警类型4002:中段加热温度超出设定温度范围
                alarm.setAlarmType(4002);
                //触发告警的图片文件名
                alarm.setAlarmPicName(fileName);
                alarmService.addAlarm(alarm);
                File alarmImageFile = new File(visionBaseDir + "\\" + AlarmTargetEnum.THERMOMETER.getAlarmTarget() + "-alarm" + "\\" + id + "\\" + fileName);
                if (!alarmImageFile.exists()) {
                    alarmImageFile.getParentFile().mkdirs();
                }
                try {
                    Files.write(bytes, alarmImageFile);
                } catch (IOException e) {
                    log.info("id={}的屏幕告警图片写入异常", id);
                }
            }
        }

        //下段温度超出设定温度值范围
        if (downTop > (downMiddle + 5)) {
            log.info(id + "号数字温度显示仪下段超出设定温度范围!");
            //先查询告警列表是否已存在告警中,并且告警类型为error的数据
            Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(4003);
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
                //告警类型4003:下段加热温度超出设定温度范围
                alarm.setAlarmType(4003);
                //触发告警的图片文件名
                alarm.setAlarmPicName(fileName);
                alarmService.addAlarm(alarm);
                File alarmImageFile = new File(visionBaseDir + "\\" + AlarmTargetEnum.THERMOMETER.getAlarmTarget() + "-alarm" + "\\" + id + "\\" + fileName);
                if (!alarmImageFile.exists()) {
                    alarmImageFile.getParentFile().mkdirs();
                }
                try {
                    Files.write(bytes, alarmImageFile);
                } catch (IOException e) {
                    log.info("id={}的屏幕告警图片写入异常", id);
                }

            }
        }

    }

    /**
     * 温度仪温度数据异常
     *
     * @param id        温度仪id
     * @param timestamp 时间戳
     * @param error     异常
     */
    @Override
    public void addThermometerDataVisionAlarm(Integer id, long timestamp, Integer error) {
        log.info(id + "号数字温度显示仪TXT文档异常!");
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
            //告警类型4003:下段加热温度超出设定温度范围
            alarm.setAlarmType(error);
            alarmService.addAlarm(alarm);
        }
    }

    /**
     * 备份过期的历史温度仪数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void copyThermometerDataHistory(long timestamp) {
        thermometerDataHistoryDao.copyThermometerDataHistory(timestamp);
    }
}
