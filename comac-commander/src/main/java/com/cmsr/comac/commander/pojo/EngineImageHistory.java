package com.cmsr.comac.commander.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/9/29 9:36
 * @description 油机图片历史实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineImageHistory implements Serializable {


    /**
     * id
     */
    private Long id;

    /**
     * 油机id
     */
    private Integer engineId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 文件名
     */
    private String filename;

}
