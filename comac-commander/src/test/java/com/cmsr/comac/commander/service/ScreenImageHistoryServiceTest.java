package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ScreenImageHistory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author ZQ
 * @create 2020-07-09
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ScreenImageHistoryServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ScreenImageHistoryService screenImageHistoryService;

    @Test
    public void testCopyScreenImageHistory() {
        screenImageHistoryService.copyScreenImageHistory(1594192731463l);
    }
}