package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ScreenImageHistory;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/10 15:31
 * @description 截屏图片历史接口
 */

public interface ScreenImageHistoryService {
    /**
     * 新增截屏图片历史数据
     *
     * @param screenImageHistory 截屏图片历史数据
     */
    void addScreenImageHistory(ScreenImageHistory screenImageHistory);

    /**
     * 查询过期的截屏图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期的截屏图片历史数据
     */
    List<ScreenImageHistory> queryScreenImageHistory(long timestamp);

    /**
     * 清理过期截屏图片历史数据
     *
     * @param timestamp 时间戳
     */
    void deleteScreenImageHistory(long timestamp);
    /**
     * 备份过期截屏图片历史数据
     *
     * @param
     */
    void copyScreenImageHistory(long timestamp);
}
