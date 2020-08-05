package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.dao.ThermometerDataHistoryDao;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.ThermometerDataHistory;
import org.junit.Before;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThermometerDataHistoryServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ThermometerDataHistoryService thermometerDataHistoryService;
    @Autowired
    ThermometerDataHistoryDao thermometerDataHistoryDao;
    @Before
    public void setUp() throws Exception {
    }

    /**
     * 测试查询数字仪表盘数据的历史记录
     */
    @Test
    public void queryThermometerDataHistoryByIdAndTimestamp() {
        ResultInfo resultInfo = thermometerDataHistoryService.queryThermometerDataHistoryByIdAndTimestamp(1l, 15632495000l, 15632499000l);
        System.out.println(resultInfo.getData());
    }
    @Test
    public void testInsert() {

            for (int i =800 ; i <=900; i+=2) {
                if (i%2==0) {
                    try {
                        Thread.sleep(5 * 1000); //设置暂停的时间 5 秒
                        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
                        thermometerDataHistory.setUpperTop(i+1);
                        thermometerDataHistory.setUpperMiddle(900);
                        thermometerDataHistory.setCentreTop(i+2);
                        thermometerDataHistory.setCentreMiddle(900);
                        thermometerDataHistory.setDownTop(i+3);
                        thermometerDataHistory.setDownMiddle(900);
                        thermometerDataHistory.setThermometerId(1);
                        thermometerDataHistory.setTimestamp(System.currentTimeMillis());
                        thermometerDataHistoryService.addThermometerDataHistory(thermometerDataHistory);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else{
                    try {
                        Thread.sleep(5 * 1000); //设置暂停的时间 5 秒
                        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
                        thermometerDataHistory.setUpperTop(i+1);
                        thermometerDataHistory.setUpperMiddle(900);
                        thermometerDataHistory.setCentreTop(i+2);
                        thermometerDataHistory.setCentreMiddle(900);
                        thermometerDataHistory.setDownTop(i+3);
                        thermometerDataHistory.setDownMiddle(900);
                        thermometerDataHistory.setThermometerId(2);
                        thermometerDataHistory.setTimestamp(System.currentTimeMillis());
                        thermometerDataHistoryService.addThermometerDataHistory(thermometerDataHistory);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                }
    }
    @Test
    public void testAddThermometer() {
        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
        thermometerDataHistory.setThermometerId(2);
        //使用获取图片时的时间戳,默认图片和数据同时获取到
        thermometerDataHistory.setTimestamp(1212121l);
        //上段顶部数值(加热温度)
        thermometerDataHistory.setUpperTop(111);
        //上段中部数值(设定温度)
        thermometerDataHistory.setUpperMiddle(111);
        //中段顶部数值(加热温度)
        thermometerDataHistory.setCentreTop(111);
        //中段中部数值(设定温度)
        thermometerDataHistory.setCentreMiddle(111);
        //下段顶部数值(加热温度)
        thermometerDataHistory.setDownTop(111);
        //下段中部数值(设定温度)
        thermometerDataHistory.setDownMiddle(111);
        thermometerDataHistoryDao.addThermometerDataHistory(thermometerDataHistory);
    }
}