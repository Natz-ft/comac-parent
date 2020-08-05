package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/10/14 9:28
 * @description 相机ocr坐标实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraOcrCoord implements Serializable {

    /**
     * 上段第一行左上角定点的x坐标
     */
    private Integer x1;

    /**
     * 上段第一行左上角定点的y坐标
     */
    private Integer y1;

    /**
     * 上段第一行的宽度
     */
    private Integer w1;

    /**
     * 上段第一行的高度
     */
    private Integer h1;

    /**
     * 上段第二行左上角定点的x坐标
     */
    private Integer x2;

    /**
     * 上段第二行左上角定点的y坐标
     */
    private Integer y2;

    /**
     * 上段第二行的宽度
     */
    private Integer w2;

    /**
     * 上段第二行的高度
     */
    private Integer h2;

    /**
     * 中段第一行左上角定点的x坐标
     */
    private Integer x3;

    /**
     * 中段第一行左上角定点的y坐标
     */
    private Integer y3;

    /**
     * 中段第一行的宽度
     */
    private Integer w3;

    /**
     * 中段第一行的高度
     */
    private Integer h3;

    /**
     * 中段第二行左上角定点的x坐标
     */
    private Integer x4;

    /**
     * 中段第二行左上角定点的y坐标
     */
    private Integer y4;

    /**
     * 中段第二行的宽度
     */
    private Integer w4;

    /**
     * 中段第二行的高度
     */
    private Integer h4;

    /**
     * 下段第一行左上角定点的x坐标
     */
    private Integer x5;

    /**
     * 下段第一行左上角定点的y坐标
     */
    private Integer y5;

    /**
     * 下段第一行的宽度
     */
    private Integer w5;

    /**
     * 下段第一行的高度
     */
    private Integer h5;

    /**
     * 下段第二行左上角定点的x坐标
     */
    private Integer x6;

    /**
     * 下段第二行左上角定点的y坐标
     */
    private Integer y6;

    /**
     * 下段第二行的宽度
     */
    private Integer w6;

    /**
     * 下段第二行的高度
     */
    private Integer h6;


}
