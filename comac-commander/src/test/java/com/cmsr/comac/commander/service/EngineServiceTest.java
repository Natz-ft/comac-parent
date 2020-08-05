package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import org.junit.Before;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EngineServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    EngineService engineService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 测试获取油机列表
     */
    @Test
    public void queryEngineList() {
        ResultInfo resultInfo = engineService.queryEngineList("实验室1");
        System.out.println(resultInfo.getData());

    }

    /**
     * 测试根据id获取油机
     */
    @Test
    public void queryEngineById() throws Exception {
        ResultInfo resultInfo = engineService.queryEngineById(2);
        boolean flag = resultInfo.isFlag();
        System.out.println(flag);
        System.out.println(resultInfo.getData());
    }

    /**
     * 测试关闭油机
     */
    @Test
    public void updateEngineEnableOrRecognitionEnableToOff() {
        Map<String, String> actionMap = new HashMap<>();
        actionMap.put("action", "disable");
        ResultInfo resultInfo = engineService.updateEngineEnableOrRecognitionEnableToOff(1, actionMap);
        System.out.println(resultInfo.isFlag());
    }
}