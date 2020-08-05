package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.EngineImageHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/8 11:43
 * @description 油机历史图片数据dao
 */

@Mapper
public interface EngineImageHistoryDao {
    /**
     * 新增油机图片历史数据
     *
     * @param engineImageHistory 油机图片历史
     */
    void addEngineImageHistory(@Param("engineImageHistory") EngineImageHistory engineImageHistory);

    /**
     * 查询清理周期之前的油机图片历史数据
     *
     * @param timestamp 时间戳
     * @return 油机图片历史数据
     */
    List<EngineImageHistory> queryEngineImageHistory(@Param("timestamp") long timestamp);

    /**
     * 清理油机图片历史数据
     *
     * @param timestamp 时间戳
     */
    void deleteEngineImageHistory(@Param("timestamp") long timestamp);


    /**
     * 获取最近一次保存的油机图片历史数据
     *
     * @param id           油机id
     * @param nowTimestamp 当前时间戳
     * @return 最近一次保存的油机图片历史数据
     */
    EngineImageHistory queryEngineImageHistoryByEngineId(@Param("id") Integer id, @Param("nowTimestamp") long nowTimestamp);
}
