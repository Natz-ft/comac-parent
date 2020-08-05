package com.cmsr.comac.commander.common;

/**
 * @author ztw
 * @data 2019/9/29 13:26
 * @description 功能开关枚举
 */

public enum ActionEnum {

    /**
     * 关闭整个功能
     */
    DISABLE("disable"),

    /**
     * 关闭视觉识别功能
     */
    RECOGNITION_DISABLE("recognition_disable");

    /**
     * 功能
     */
    private String action;

    /**
     * 构造
     *
     * @param action 功能
     */
    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
