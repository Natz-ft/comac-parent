package com.cmsr.comac.commander.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.service.VidiconService;
import com.cmsr.comac.commander.util.IpAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author ZQ
 * @create 2020-06-30
 */
@RestController
@RequestMapping("comac-commander/v2.0/vidicon")
@Slf4j
public class VidiconController {
    @Autowired
    VidiconService vidiconService;

    @PostMapping("/list")
    public ResultInfo getVidiconList(HttpServletRequest request, @RequestBody Map map) {
        String ipAddress = IpAddressUtil.getIpAddress(request);
        String bjsw = request.getHeader("bjsw");
        Long timestamp = (Long) map.get("timestamp");
        System.out.println(map.get("timestamp"));
        ResultInfo vidiconList = vidiconService.getVidiconList(ipAddress, timestamp, bjsw);
        return vidiconList;
    }

}
