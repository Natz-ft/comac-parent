package com.cmsr.comac.commander.service;

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
public class ThermometerImageHistoryServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ThermometerImageHistoryService thermometerImageHistoryService;

    @Test
    public void testCopyThermometerImageHistory() {
        thermometerImageHistoryService.copyThermometerImageHistory(1594192730450l);
    }
}