package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.ScreenImageHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/11 14:55
 * @description 截屏图片历史数据dao
 */

@Mapper
public interface ScreenImageHistoryDao {


    /**
     * 新增截屏图片历史数据
     *
     * @param screenImageHistory 截屏图片历史数据
     */
    void addScreenImageHistory(@Param("screenImageHistory") ScreenImageHistory screenImageHistory);

    /**
     * 查询过期的截屏图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期的截屏图片历史数据
     */
    List<ScreenImageHistory> queryScreenImageHistory(@Param("timestamp") long timestamp);

    /**
     * 清理过期截屏图片历史数据
     *
     * @param timestamp 时间戳
     */
    void deleteScreenImageHistory(@Param("timestamp") long timestamp);

    /**
     * 获取截屏图片历史数据
     *
     * @param id           屏幕id
     * @param nowTimestamp 当前时间戳
     * @return 截屏图片历史数据
     */
    ScreenImageHistory queryScreenImageHistoryByScreenId(@Param("id") Integer id, @Param("nowTimestamp") long nowTimestamp);

    /**
     * 备份过期截屏图片历史数据
     *
     * @param timestamp 时间戳
     */
    void copyScreenImageHistory(long timestamp);
}
