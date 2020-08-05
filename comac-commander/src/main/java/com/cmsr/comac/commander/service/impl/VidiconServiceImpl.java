package com.cmsr.comac.commander.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmsr.comac.commander.dao.VidiconDao;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.RsaToken;
import com.cmsr.comac.commander.pojo.Vidicon;
import com.cmsr.comac.commander.service.VidiconService;
import com.cmsr.comac.commander.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author ZQ
 * @create 2020-07-02
 */
@Service
@Slf4j
public class VidiconServiceImpl implements VidiconService {
    /**
     * 私钥保存路径
     */
    @Value("${privateKeyPath}")
    String privateKeyPath;

    @Autowired
    VidiconDao vidiconDao;

    @Override
    public ResultInfo getVidiconList(String ipAddr, Long timestamp, String data) {
        //rsa解密
        String decodeStr = null;
        try {
            decodeStr = RSAUtils.decryptByPrivateKey(data, privateKeyPath);
            log.info("解密后的字符串:{}", decodeStr);
        } catch (Exception e) {
            log.info("解密异常");
            return new ResultInfo(false, "获取摄像机列表失败");
        }
        if (StringUtils.isNotEmpty(decodeStr)) {
            RsaToken rsaToken = JSON.parseObject(decodeStr, RsaToken.class);
            String ip = rsaToken.getIp();
            long time = rsaToken.getTimestamp();
            //对客户端的ip和rsa加密的ip进行比较
            if (ip.equals(ipAddr) & timestamp.equals(time)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime minus = now.minus(10000, ChronoUnit.SECONDS);
                //判断请求是否超时
                if (minus.isBefore(LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8)))) {
                    List<Vidicon> vidicons = vidiconDao.getVidiconList();
                    return new ResultInfo(true, vidicons, "获取摄像机列表成功");
                }
            }
        }
        return new ResultInfo(false, "获取摄像机列表失败");
    }

    @Override
    public ResultInfo getVidiconList() {
        List<Vidicon> vidiconList = vidiconDao.getVidiconList();
        return new ResultInfo(true, vidiconList);
    }
}
