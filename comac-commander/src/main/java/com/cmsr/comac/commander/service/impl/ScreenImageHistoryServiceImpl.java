package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.dao.ScreenImageHistoryDao;
import com.cmsr.comac.commander.pojo.ScreenImageHistory;
import com.cmsr.comac.commander.service.ScreenImageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/10 15:31
 * @description 截屏图片历史实现
 */

@Service
public class ScreenImageHistoryServiceImpl implements ScreenImageHistoryService {
    /**
     * 截屏图片历史dao
     */
    @Autowired
    private ScreenImageHistoryDao screenImageHistoryDao;

    /**
     * 新增截屏图片历史数据
     *
     * @param screenImageHistory 截屏图片历史数据
     */
    @Override
    public void addScreenImageHistory(ScreenImageHistory screenImageHistory) {
        screenImageHistoryDao.addScreenImageHistory(screenImageHistory);
    }

    /**
     * 查询过期的截屏图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期的截屏图片历史数据
     */
    @Override
    public List<ScreenImageHistory> queryScreenImageHistory(long timestamp) {
        return screenImageHistoryDao.queryScreenImageHistory(timestamp);
    }

    /**
     * 清理过期截屏图片历史数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void deleteScreenImageHistory(long timestamp) {
        screenImageHistoryDao.deleteScreenImageHistory(timestamp);
    }
    /**
     * 备份过期截屏图片历史数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void copyScreenImageHistory(long timestamp) {
        screenImageHistoryDao.copyScreenImageHistory(timestamp);
    }
}
