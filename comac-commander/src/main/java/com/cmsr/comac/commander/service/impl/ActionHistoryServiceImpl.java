package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.dao.ActionHistoryDao;
import com.cmsr.comac.commander.pojo.ActionHistory;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.service.ActionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ztw
 * @data 2019/9/30 16:29
 * @description 用户操作历史实现
 */
@Slf4j
@Service
public class ActionHistoryServiceImpl implements ActionHistoryService {
    /**
     * 用户操作历史dao
     */
    @Autowired
    private ActionHistoryDao actionHistoryDao;

    /**
     * 返回用户操作记录
     *
     * @param username 用户名
     * @return 用户操作记录
     */
    @Override
    public ResultInfo queryActionHistoryByUsername(String username) {
        List<ActionHistory> actionHistoryList = actionHistoryDao.queryActionHistoryByUsername(username);
        if (CollectionUtils.isEmpty(actionHistoryList)) {
            return new ResultInfo(false, "无操作记录数据!");
        }
        return new ResultInfo(true, actionHistoryList);
    }

    /**
     * 新增人员访问记录
     *
     * @param username 用户名
     * @param action   功能
     * @throws Exception 异常
     */
    @Override
    public void addActionHistory(String username, String action) throws Exception {
        ActionHistory actionHistory = new ActionHistory();
        actionHistory.setUsername(username);
        actionHistory.setAction(action);
        //获取时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringTime = sdf.format(new Date());
        long timestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
        actionHistory.setCreatedAt(timestamp);

        actionHistoryDao.addActionHistory(actionHistory);

    }
}
