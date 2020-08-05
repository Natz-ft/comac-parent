package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.EngineImageHistory;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/8 11:42
 * @description 油机图片历史接口
 */

public interface EngineImageHistoryService {
    /**
     * 新增油机图片历史数据
     *
     * @param engineImageHistory 油机图片历史数据
     */
    void addEngineImageHistory(EngineImageHistory engineImageHistory);

    /**
     * 过期油机图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期油机图片历史数据
     */
    List<EngineImageHistory> queryEngineImageHistory(long timestamp);

    /**
     * 清理油机图片历史数据
     *
     * @param timestamp 时间戳
     */
    void deleteEngineImageHistory(long timestamp);
}
