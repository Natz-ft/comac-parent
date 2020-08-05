package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.CodeEnum;
import com.cmsr.comac.commander.common.Director;
import com.cmsr.comac.commander.common.ResponseData;
import com.cmsr.comac.commander.pojo.Experiment;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.service.ExperimentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/24 12:35
 * @description 实验总开关controller
 */
@RestController
@RequestMapping("/comac-commander/v2.0/experiment")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ExperimentController {

    /**
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 总开关业务
     */
    @Autowired
    private ExperimentService experimentService;


    /**
     * 获取实验开关列表
     *
     * @param location 实验室
     * @param request  请求
     * @return 开关列表
     */
    @GetMapping
    public ResponseData getExperimentActionList(@RequestParam("location") String location, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }

        //获取实验列表
        ResultInfo experimentListResult = experimentService.getExperimentActionList(location);
        //异常
        if (!experimentListResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, experimentListResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, experimentListResult.getData());
    }

    /**
     * 开、关截屏，油机，温度仪功能
     *
     * @param id        开关id
     * @param actionMap 功能map
     * @param request   请求
     * @return 开关
     */
    @PostMapping("/{id}/action")
    @Director
    public ResponseData onOrOffExperiment(@PathVariable("id") Integer id, @RequestBody Map<String, String> actionMap, HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            log.info("无token或token错误!");
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //根据id获取实验开关
        ResultInfo experimentActionResult = experimentService.getExperimentActionById(id);
        //异常
        if (!experimentActionResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, experimentActionResult.getMsg());
        }

        //获取实验开关数据
        Experiment experiment = (Experiment) experimentActionResult.getData();

        HttpSession session = request.getSession();

        //开/关实验
        ResultInfo onOrOffExperimentResult = experimentService.onOrOffExperiment(id, experiment, actionMap, session);

        //异常
        if (!onOrOffExperimentResult.isFlag()) {
            return ResponseData.out(CodeEnum.ERROR, onOrOffExperimentResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, onOrOffExperimentResult.getMsg());
    }


}
