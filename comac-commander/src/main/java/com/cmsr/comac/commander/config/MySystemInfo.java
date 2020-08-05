package com.cmsr.comac.commander.config;

import com.vimalselvam.testng.SystemInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zuozwei
 * @Date: 2018/12/15 00:54
 * @Description:
 */
public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {

        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("测试人员", "洪硕");

        return systemInfo;
    }
}
