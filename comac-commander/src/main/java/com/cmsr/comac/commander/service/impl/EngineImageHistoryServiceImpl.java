package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.dao.EngineImageHistoryDao;
import com.cmsr.comac.commander.pojo.EngineImageHistory;
import com.cmsr.comac.commander.service.EngineImageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/8 11:42
 * @description 油机图片历史实现
 */

@Service
public class EngineImageHistoryServiceImpl implements EngineImageHistoryService {
    /**
     * 油机图片历史dao
     */
    @Autowired
    private EngineImageHistoryDao engineImageHistoryDao;


    /**
     * 新增油机图片历史数据
     *
     * @param engineImageHistory 油机图片历史
     */
    @Override
    public void addEngineImageHistory(EngineImageHistory engineImageHistory) {
        engineImageHistoryDao.addEngineImageHistory(engineImageHistory);
    }

    /**
     * 过期油机图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期油机图片历史数据
     */
    @Override
    public List<EngineImageHistory> queryEngineImageHistory(long timestamp) {
        return engineImageHistoryDao.queryEngineImageHistory(timestamp);
    }

    /**
     * 清理油机图片历史数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void deleteEngineImageHistory(long timestamp) {
        engineImageHistoryDao.deleteEngineImageHistory(timestamp);
    }
}
