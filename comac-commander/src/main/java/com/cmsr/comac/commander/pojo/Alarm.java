package com.cmsr.comac.commander.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author ztw
 * @data 2019/9/24 18:02
 * @description 告警实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alarm implements Serializable {
    /**
     * 告警id
     */
    private Long alarmId;

    /**
     * 告警的目标，枚举型，只能为thermometer，engine或screen
     */
    private String alarmTarget;

    /**
     * 告警目标的id,即对应的告警的截屏,油机,数字温度显示仪的id
     */
    private Integer alarmTargetId;

    /**
     * 告警类型,需要在配置文件中配置所有的告警类型
     */
    private Integer alarmType;

    /**
     * 告警状态,,1-告警中,2-告警关闭
     */
    private Integer alarmStatus;

    /**
     * 告警发生的时间
     */
    private Long alarmTime;

    /**
     * 告警处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp alarmHandleTime;

    /**
     * 告警处理人用户名,没人处理为null
     */
    private User alarmHandlerPerson;

    /**
     * 告警关联的图片文件名
     */
    private String alarmPicName;

}
