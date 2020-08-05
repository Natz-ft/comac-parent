package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.common.OcrResponse;
import com.cmsr.comac.commander.dao.ThermometerDao;
import com.cmsr.comac.commander.dao.ThermometerImageHistoryDao;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.Thermometer;
import com.cmsr.comac.commander.pojo.ThermometerImageHistory;
import org.json.JSONObject;
import org.junit.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThermometerServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ThermometerService thermometerService;
    @Autowired
    ThermometerImageHistoryDao thermometerDao;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 测试获取温度仪表盘列表
     */
    @Test
    public void queryThermometerList() {
        ResultInfo resultInfo = thermometerService.queryThermometerList("实验室1");
        System.out.println(resultInfo.getData());
    }

    /**
     * 测试根据id获取温度仪表盘
     */
    @Test
    public void queryThermometerById() throws Exception {
//        ResultInfo resultInfo = thermometerService.queryThermometerById(2);
//        System.out.println(resultInfo.getData());
        ThermometerImageHistory thermometerImageHistory = thermometerDao.queryThermometerImageHistoryByThermometerId(1, 1222222222);
        System.out.println(thermometerImageHistory==null);
    }

    /**
     * 测试获取数字温度显示仪文档数据
     */
    @Test
    public void getThermometerTxtData() throws Exception {
        ResultInfo thermometerTxtData = thermometerService.getThermometerTxtData(1);
        System.out.println(thermometerTxtData.getData());

    }

    /**
     * 测试关闭某个数字温度仪表盘的采集
     */
    @Test
    public void updateThermometerEnableOrEngineRecognitionEnableToOff() {
        Map<String, String> actionMap = new HashMap<>();
        actionMap.put("action", "disable");
        ResultInfo resultInfo = thermometerService.updateThermometerEnableOrEngineRecognitionEnableToOff(1, actionMap);
        System.out.println(resultInfo.isFlag());
    }
}