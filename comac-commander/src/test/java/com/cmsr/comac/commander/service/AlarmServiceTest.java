package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.util.Md5Util;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AlarmServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    AlarmService alarmService;

    /**
     * 测试查询告警列表
     */
    @Test
    public void queryAlarmList() {
        ResultInfo resultInfo = alarmService.queryAlarmList(1, 2, 1, "screen", 0l, 15870226250000l);
        System.out.println(resultInfo.getData());
        Assert.notNull(resultInfo.getData(), "成功");
    }

    /**
     * 测试根据id查询告警详情
     */
    @Test
    public void queryAlarmByAlarmId() {
        ResultInfo resultInfo = alarmService.queryAlarmByAlarmId(1957l);
        System.out.println(resultInfo.getData());
        Assert.notNull(resultInfo.getData(), "成功");
    }

    /**
     * 测试关闭某个告警
     */
    @Test
    public void updateAlarmStatusToOff() {
        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("status", 2);
        ResultInfo resultInfo = alarmService.updateAlarmStatusToOff(1957l, statusMap, "zhangsan");
        System.out.println(resultInfo.isFlag());
        Assert.notNull(resultInfo.getData(), "成功");
    }

    /**
     * 测试获取某个告警关联的原始图片
     */
    @Test
    public void getAlarmConnectImage() throws Exception {
        Alarm alarm = new Alarm();
        alarm.setAlarmStatus(1);
        alarm.setAlarmTarget("screen");
        alarm.setAlarmPicName("2.jpg");

        ResultInfo resultInfo = alarmService.getAlarmConnectImage(alarm);
        System.out.println(resultInfo.getMsg());
        Assert.notNull(resultInfo.getData(), "成功");
    }

    @Test
    public void testMD5() {
        String md5 = Md5Util.stringToMd5("123456");
        System.out.println(md5);
    }

    @Test
    public void testInsertAlarm() {
        for (int i = 31; i <= 60; i++) {
            if (i % 4 == 1) {
                Alarm alarm = new Alarm();
                alarm.setAlarmPicName(i + ".jpg");
                alarm.setAlarmTarget("screen");
                alarm.setAlarmStatus(0);
                alarm.setAlarmType(4001);
                alarm.setAlarmTargetId(1);
                alarm.setAlarmTime(System.currentTimeMillis());
                alarmService.addAlarm(alarm);
            }
            if (i % 4 == 2) {
                Alarm alarm = new Alarm();
                alarm.setAlarmPicName(i + ".jpg");
                alarm.setAlarmTarget("screen");
                alarm.setAlarmStatus(0);
                alarm.setAlarmType(4002);
                alarm.setAlarmTargetId(1);
                alarm.setAlarmTime(System.currentTimeMillis());
                alarmService.addAlarm(alarm);
            }
            if (i % 4 == 3) {
                Alarm alarm = new Alarm();
                alarm.setAlarmPicName(i + ".jpg");
                alarm.setAlarmTarget("thermometer");
                alarm.setAlarmStatus(0);
                alarm.setAlarmType(3001);
                alarm.setAlarmTargetId(2);
                alarm.setAlarmTime(System.currentTimeMillis());
                alarmService.addAlarm(alarm);
            }
            if (i % 4 == 0) {
                Alarm alarm = new Alarm();
                alarm.setAlarmPicName(i + ".jpg");
                alarm.setAlarmTarget("thermometer");
                alarm.setAlarmStatus(0);
                alarm.setAlarmType(3002);
                alarm.setAlarmTargetId(2);
                alarm.setAlarmTime(System.currentTimeMillis());
                alarmService.addAlarm(alarm);
            }

        }
    }

    @Test
    public void testCopyAlarmByTimestamp() {
        alarmService.copyAlarmByTimestamp(1594188952674l);
    }
}