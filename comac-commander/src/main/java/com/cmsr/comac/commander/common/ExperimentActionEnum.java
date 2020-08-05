package com.cmsr.comac.commander.common;

/**
 * @author ztw
 * @data 2019/10/24 12:47
 * @description 实验开关枚举类
 */

public enum ExperimentActionEnum {

    /**
     * 关闭采集
     */
    DISABLE("disable"),

    /**
     * 开启采集
     */
    ENABLE("enable"),

    /**
     * 关闭视觉识别
     */
    RECOGNITION_DISABLE("recognition_disable"),

    /**
     * 开启视觉识别
     */
    RECOGNITION_ENABLE("recognition_enable");

    /**
     * 功能
     */
    private String action;

    /**
     * 构造
     *
     * @param action 功能
     */
    ExperimentActionEnum(String action) {
        this.action = action;
    }

    /**
     * get
     *
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * set
     *
     * @param action action
     */
    public void setAction(String action) {
        this.action = action;
    }
}
