package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.Screen;
import com.cmsr.comac.commander.pojo.ScreenImageHistory;
import org.junit.Before;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScreenServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ScreenService screenService;
    @Autowired
    ScreenImageHistoryService screenImageHistoryService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 测试获取截屏列表
     */
    @Test
    public void queryScreenList() {
        ResultInfo resultInfo = screenService.queryScreenList("实验室1");
        System.out.println(resultInfo.getData());
    }

    /**
     * 测试根据id查询截屏
     */
    @Test
    public void queryScreenById() throws Exception {
        ResultInfo resultInfo = screenService.queryScreenById(1);
        System.out.println(resultInfo.getData());
        Reporter.log("这是故意写入的日志");

    }

    /**
     * 测试关闭屏幕视觉识别
     */
    @Test
    public void updateScreenEnableOrRecognitionEnableToOff() {
        Map<String, String> actionMap = new HashMap<>();
        actionMap.put("action", "recognition_disable");
        ResultInfo resultInfo = screenService.updateScreenEnableOrRecognitionEnableToOff(1, actionMap);
        System.out.println(resultInfo.isFlag());

    }

    /**
     * 测试获取截屏图片
     */
    @Test
    public void getScreenImage() {
        Screen screen = new Screen();
        screen.setId(1);
        screen.setUrl("http://localhost:1000/");
        ResultInfo screenImage = screenService.getScreenImage(screen);
        System.out.println(screenImage.getData());
    }

    /**
     * 测试压缩图片
     */
    @Test
    public void compressImage() {
        File picture = new File("E:\\桌面\\1.jpg");
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(picture);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultInfo resultInfo = screenService.compressImage(buffer);
        System.out.println(resultInfo.isFlag());
    }

}