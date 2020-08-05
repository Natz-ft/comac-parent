package com.cmsr.comac.scout.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * @author ztw
 * @use 利用Java代码给图片加水印
 */
public class WaterMarkUtils {

    /**
     * @param srcImg           源图片路径
     * @param waterMarkContent 水印内容
     * @param markContentColor 水印颜色
     * @param font             水印字体
     */
    public static BufferedImage addWaterMark(BufferedImage srcImg, String waterMarkContent, Color markContentColor, Font font) {
        try {
            // 读取原图片信息
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor);
            g.setFont(font);
            //设置水印的坐标
            int x = srcImgWidth - getWatermarkLength(waterMarkContent, g);
            int y = srcImgHeight;
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片
            return bufImg;
        } catch (Exception e) {
            return null;
        }

    }

    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

}
