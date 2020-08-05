package com.cmsr.comac.scout.controller;


import com.cmsr.comac.scout.util.WaterMarkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;


/**
 * @author ztw
 * @data 2019/8/17 16:09
 * @description
 */

@RestController
@RequestMapping("/comac-scout/v2.0")
public class ScoutController {
    /**
     * token
     */
    @Value("${screenToken}")
    private String screenToken;

    /**
     * 截屏
     *
     * @param request 请求
     * @return 截屏
     * @throws Exception 异常
     */
    @GetMapping("/screen")
    public byte[] getCaptureScreen(HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (screenToken.equals(token)) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            byte[] bytes = new byte[0];
            try {
                Robot robot = new Robot();
                //获取到截屏
                BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                //调用方法添加水印
                Font font = new Font("微软雅黑", Font.PLAIN, 35);
                //获取IP地址
                String localIp = InetAddress.getLocalHost().getHostAddress();
                BufferedImage waterMarkImage = WaterMarkUtils.addWaterMark(image, localIp, Color.YELLOW, font);

                byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(waterMarkImage, "jpg", byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
            } catch (AWTException e) {
                e.printStackTrace();
            } catch (HeadlessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //释放资源
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.close();
                }
            }
            return bytes;
        } else {
            //token不一致,返回空数组
            return new byte[0];
        }
    }
}
