package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;


/**
 * @author ztw
 * @data 2019/9/30 16:29
 * @description 用户操作历史接口
 */

public interface ActionHistoryService {
    /**
     * 返回用户操作记录
     *
     * @param username 用户名
     * @return 用户操作记录
     */
    ResultInfo queryActionHistoryByUsername(String username);

    /**
     * 新增人员操作记录
     *
     * @param username 用户名
     * @param action   功能
     * @throws Exception 异常
     */
    void addActionHistory(String username, String action) throws Exception;
}
