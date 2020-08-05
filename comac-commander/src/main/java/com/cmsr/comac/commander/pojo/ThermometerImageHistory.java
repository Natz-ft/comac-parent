package com.cmsr.comac.commander.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/9/29 9:41
 * @description 温度仪图片历史实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThermometerImageHistory implements Serializable {

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
     * 文件名
     */
    private String filename;

}
