package com.cmsr.comac.commander.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ztw
 * @data 2019/9/29 8:54
 * @description 截屏实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screen implements Serializable {

    /**
     * 屏幕id
     */
    private Integer id;

    /**
     * 屏幕描述
     */
    private String description;

    /**
     * 截屏url地址
     */
    private String url;

    /**
     * 屏幕所在实验室
     */
    private String location;

    /**
     * 是否启用功能，0代表不启用，1代表启用，默认值为1
     */
    private Integer enable;

    /**
     * 是否启用视觉识别，0代表不启用，1代表启用，默认值为0
     */
    private Integer recognitionEnable;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updatedAt;

    /**
     * 0表示正常，1表示删除
     */
    private Integer deleted;

}
