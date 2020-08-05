package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.Vidicon;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author ZQ
 * @create 2020-07-08
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class VidiconServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    VidiconService vidiconService;

    @Test
    public void testGetVidiconList() {
        ResultInfo vidiconList = vidiconService.getVidiconList();
        List<Vidicon> vidicons = (List<Vidicon>) vidiconList.getData();
        vidicons.forEach(vidicon -> {
            System.out.println(vidicon);
        });
    }
    @Test
    public void test11() {
        long l = System.currentTimeMillis();
        System.out.println(new Date().getTime());
        System.out.println(l);
        System.out.println(LocalDateTime.ofEpochSecond(l, 0, ZoneOffset.ofHours(8)));
        System.out.println(LocalDateTime.now());
    }
}