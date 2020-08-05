package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.ExperimentActionEnum;
import com.cmsr.comac.commander.dao.*;
import com.cmsr.comac.commander.pojo.Experiment;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.service.ActionHistoryService;
import com.cmsr.comac.commander.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/24 13:12
 * @description 实验开关实现
 */

@Service
public class ExperimentServiceImpl implements ExperimentService {
    /**
     * 实验开关dao
     */
    @Autowired
    private ExperimentDao experimentDao;
    /**
     * 截屏dao
     */
    @Autowired
    private ScreenDao screenDao;
    /**
     * 油机dao
     */
    @Autowired
    private EngineDao engineDao;
    /**
     * 温度仪dao
     */
    @Autowired
    private ThermometerDao thermometerDao;
    /**
     * 用户操作历史dao
     */
    @Autowired
    private ActionHistoryService actionHistoryService;


    /**
     * 获取实验开关列表
     *
     * @param location 实验室
     * @return 实验开关列表
     */
    @Override
    public ResultInfo getExperimentActionList(String location) {
        //查询实验开关列表数据库
        List<Experiment> experimentList = experimentDao.getExperimentActionList(location);
        //异常
        if (CollectionUtils.isEmpty(experimentList)) {
            return new ResultInfo(false, "未获取到数据！");
        }
        return new ResultInfo(true, experimentList);
    }

    /**
     * 根据id查询实验开关
     *
     * @param id 实验开关id
     * @return 实验开关
     */
    @Override
    public ResultInfo getExperimentActionById(Integer id) {
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (null == experiment) {
            return new ResultInfo(false, "未获取到数据！");
        }
        return new ResultInfo(true, experiment);
    }

    /**
     * 开、关截屏，油机，温度仪功能
     *
     * @param id         实验开关id
     * @param experiment 实验开关
     * @param actionMap  map
     * @param session    session
     * @return result
     */
    @Override
    public ResultInfo onOrOffExperiment(Integer id, Experiment experiment, Map<String, String> actionMap, HttpSession session) {
        //获取参数
        String action = actionMap.get("action");
        //关闭
        if (ExperimentActionEnum.DISABLE.getAction().equals(action)) {
            try {
                //关闭实验
                experimentDao.updateEnableToOff(id);
                //关闭截屏采集功能
                screenDao.updateScreenEnableToOff(experiment.getScreenId());
                //关闭油机采集功能
                engineDao.updateEngineEnableToOff(experiment.getEngineId());
                //关闭数字温度仪采集功能
                thermometerDao.updateThermometerEnableToOff(experiment.getThermometerId());
                //记录操作
                User userSession = (User) session.getAttribute("user");
                actionHistoryService.addActionHistory(userSession.getUsername(), "关闭" + id + "号实验总开关");
                return new ResultInfo(true, "关闭成功!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "服务器异常，关闭失败！");
            }
        } else if (ExperimentActionEnum.ENABLE.getAction().equals(action)) {
            try {
                //开启实验
                experimentDao.updateEnableToOn(id);
                //开启截屏采集功能
                screenDao.updateScreenEnableToOn(experiment.getScreenId());
                //开启截屏视觉识别功能
                screenDao.updateScreenRecognitionEnableToOn(experiment.getScreenId());
                //开启油机采集功能
                engineDao.updateEngineEnableToOn(experiment.getEngineId());
                //开启油机视觉识别功能
                engineDao.updateEngineRecognitionEnableToOn(experiment.getScreenId());
                //开启数字温度仪采集功能
                thermometerDao.updateThermometerEnableToOn(experiment.getThermometerId());
                //开启数字温度仪视觉识别功能
                thermometerDao.updateEngineRecognitionEnableToOn(experiment.getScreenId());
                //记录操作
                User userSession = (User) session.getAttribute("user");
                actionHistoryService.addActionHistory(userSession.getUsername(), "开启" + id + "号实验总开关");
                return new ResultInfo(true, "开启成功!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "服务器异常，开启失败！");
            }
        } else {
            return new ResultInfo(false, "参数错误！");
        }
    }
}
