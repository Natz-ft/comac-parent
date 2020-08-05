package com.cmsr.comac.commander.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/9/29 9:39
 * @description 截屏图片历史实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenImageHistory implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 屏幕id
     */
    private Integer screenId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 文件名
     */
    private String filename;


}
