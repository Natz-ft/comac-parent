package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/10/24 13:02
 * @description 实验开关实体类
 */

@Data
public class Experiment implements Serializable {

    /**
     * 实验开关id
     */
    private Integer id;

    /**
     * 屏幕id
     */
    private Integer screenId;

    /**
     * 油机id
     */
    private Integer engineId;

    /**
     * 数字温度仪id
     */
    private Integer thermometerId;

    /**
     * 实验开关
     */
    private Integer enable;

    /**
     * 实验室
     */
    private String location;
}
