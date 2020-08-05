package com.cmsr.comac.commander.common;


/**
 * @author ztw
 * @data 2019/9/24 20:19
 * @description 告警目标枚举类
 */


public enum AlarmTargetEnum {

    /**
     * 截屏目标
     */
    SCREEN("screen"),

    /**
     * 油机
     * 目标
     */
    ENGINE("engine"),

    /**
     * 温度仪目标
     */
    THERMOMETER("thermometer"),

    /**
     * 视觉文件目标
     */
    VISION("vision");

    /**
     * 告警目标
     */
    private String alarmTarget;

    /**
     * 构造
     *
     * @param alarmTarget 告警类型
     */
    AlarmTargetEnum(String alarmTarget) {
        this.alarmTarget = alarmTarget;
    }

    /**
     * get
     *
     * @return alarmTarget
     */
    public String getAlarmTarget() {
        return alarmTarget;
    }

    /**
     * set
     *
     * @param alarmTarget 告警类型
     */
    public void setAlarmTarget(String alarmTarget) {
        this.alarmTarget = alarmTarget;
    }
}
