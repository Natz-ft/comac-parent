package com.cmsr.comac.commander.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.util.Md5Util;
import com.google.common.io.Files;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMethodsDemo extends AbstractTestNGSpringContextTests {
    @Autowired
    UserService userService;

    @Test
    public void test1() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void test2() {
        Assert.assertEquals(1, 1);
    }


    @Test
    public void test3() {
        Assert.assertEquals("aaa", "aaa");
    }


    @Test
    public void logDemo() {
        Reporter.log("这是故意写入的日志");
        throw new RuntimeException("故意运行时异常");
    }

    @Test
    public void test111() {
        long l = System.currentTimeMillis();
        System.out.println(l);
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", l);
        map.put("ip", "117.186.242.158");
        String string = JSON.toJSONString(map);
        System.out.println(string);
    }

    @Test
    public void testFileCopy() {

    }
}
