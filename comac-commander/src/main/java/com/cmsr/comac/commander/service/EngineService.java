package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;

import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 10:16
 * @description 油机接口
 */

public interface EngineService {

    /**
     * 查询油机列表
     *
     * @param location 实验室
     * @return 油机列表
     */
    ResultInfo queryEngineList(String location);

    /**
     * 根据id查询油机对象
     *
     * @param id 油机id
     * @return 油机
     * @throws Exception 异常
     */
    ResultInfo queryEngineById(Integer id) throws Exception;

    /**
     * 关闭某个油机的采集
     *
     * @param id        油机id
     * @param actionMap map
     * @return result
     */
    ResultInfo updateEngineEnableOrRecognitionEnableToOff(Integer id, Map<String, String> actionMap);
}
