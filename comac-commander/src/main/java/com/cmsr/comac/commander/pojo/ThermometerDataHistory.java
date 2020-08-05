package com.cmsr.comac.commander.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/9/29 9:41
 * @description 温度仪历史数据实体类
 */

@Data
public class ThermometerDataHistory implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 数字温度仪表盘id
     */
    private Integer thermometerId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 上段顶部数值
     */
    private Integer upperTop;

    /**
     * 上段中部数值
     */
    private Integer upperMiddle;

    /**
     * 中段顶部数值
     */
    private Integer centreTop;

    /**
     * 中段中部数值
     */
    private Integer centreMiddle;

    /**
     * 下段顶部数值
     */
    private Integer downTop;

    /**
     * 下段中部数值
     */
    private Integer downMiddle;

}
