package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.Experiment;
import com.cmsr.comac.commander.pojo.ResultInfo;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/24 13:12
 * @description 实验开关接口
 */

public interface ExperimentService {
    /**
     * 获取实验开关列表
     *
     * @param location 实验室
     * @return 实验开关列表
     */
    ResultInfo getExperimentActionList(String location);

    /**
     * 根据id查询实验开关
     *
     * @param id 开关id
     * @return 开关
     */
    ResultInfo getExperimentActionById(Integer id);

    /**
     * 开、关截屏，油机，温度仪功能
     *
     * @param id         开关id
     * @param experiment 实验开关
     * @param actionMap  map
     * @param session    session
     * @return result
     */
    ResultInfo onOrOffExperiment(Integer id, Experiment experiment, Map<String, String> actionMap, HttpSession session);
}
