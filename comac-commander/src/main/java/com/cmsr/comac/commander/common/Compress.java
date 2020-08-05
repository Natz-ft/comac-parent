package com.cmsr.comac.commander.common;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author nj
 * @data 2019/8/20
 * @description 图片压缩
 */
public class Compress {
    /**
     * 将图片压缩到指定大小以内
     *
     * @param srcImgData 源图片数据
     * @return 压缩后的图片数据
     */
    public static byte[] compressUnderSize(byte[] srcImgData, double scale) throws Exception {
        byte[] imgData = Arrays.copyOf(srcImgData, srcImgData.length);
        imgData = compress(imgData, scale);
        return imgData;
    }

    /**
     * 按照 宽高 比例压缩
     *
     * @param srcImgData 待压缩图片数据
     * @param scale      压缩刻度
     * @return 压缩后图片数据
     * @throws IOException 压缩图片过程中出错
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
        int width = (int) (bi.getWidth() * scale);
        int height = (int) (bi.getHeight() * scale);

        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(tag, "JPEG", out);
        return out.toByteArray();
    }

}
