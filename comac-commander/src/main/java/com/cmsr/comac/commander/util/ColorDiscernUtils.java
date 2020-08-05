package com.cmsr.comac.commander.util;

import java.awt.image.BufferedImage;


/**
 * @author ztw
 * @data 2019/10/24 21:45
 * @description rgb值工具类
 */

public class ColorDiscernUtils {

    public static int[] getRgbData(BufferedImage bufferedImage) {
        int[] rgb = new int[3];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        //取图片中心像素点
        int centrePixel = bufferedImage.getRGB(width / 2, height / 2);
        rgb[0] = (centrePixel & 0xff0000) >> 16;
        rgb[1] = (centrePixel & 0xff00) >> 8;
        rgb[2] = (centrePixel & 0xff);
        return rgb;
    }


}
