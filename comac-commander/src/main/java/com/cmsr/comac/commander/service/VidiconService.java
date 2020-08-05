package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;

/**
 * @author ZQ
 * @create 2020-07-02
 */
public interface VidiconService {

    /**
     * @Description 获取摄像机列表
     * @Param ipAddr 客户端ip， timestamp 时间戳 ，data 加密数据
     * @Return
     */
    ResultInfo getVidiconList(String ipAddr, Long timestamp, String data);

    /**
     * 查询数据库中的所有网络摄像机
     *
     * @return
     */
    ResultInfo getVidiconList();
}
