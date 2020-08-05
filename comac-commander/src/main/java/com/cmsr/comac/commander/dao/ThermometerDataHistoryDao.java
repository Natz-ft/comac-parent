package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.ThermometerDataHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/29 18:02
 * @description 温度仪历史数据dao
 */

@Mapper
public interface ThermometerDataHistoryDao {
    /**
     * 返回数字仪表盘数据的历史记录
     *
     * @param id        温度仪id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 温度仪历史数据
     */
    List<ThermometerDataHistory> queryThermometerDataHistoryByIdAndTimestamp(@Param("id") Long id, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 新增数字温度显示仪的仪表盘数据
     *
     * @param thermometerDataHistory 温度仪历史数据
     */
    void addThermometerDataHistory(@Param("thermometerDataHistory") ThermometerDataHistory thermometerDataHistory);

    /**
     * 清理数字温度显示仪周期之外的仪表盘数值数据
     *
     * @param timestamp 时间戳
     */
    void deleteThermometerDataHistory(@Param("timestamp") long timestamp);

    /**
     * 获取距离当前时间最近的一条温度仪历史数据记录
     *
     * @param id           温度仪id
     * @param nowTimestamp 当前时间戳
     * @return 最近的一条温度仪历史数据记录
     */
    ThermometerDataHistory queryThermometerDataHistoryByThermometerId(@Param("id") Integer id, @Param("nowTimestamp") long nowTimestamp);

    /**
     * 查询id对应记录之前的4条记录
     *
     * @param id
     * @return
     */
    List<ThermometerDataHistory> queryThermometerDataHistoryById(@Param("id") Long id, @Param("times") Integer times);

    /**
     * 备份数字温度显示仪周期之外的仪表盘数值数据
     *
     * @param timestamp 时间戳
     */
    void copyThermometerDataHistory(@Param("timestamp") long timestamp);

    /**
     * @param id
     * @return com.cmsr.comac.commander.pojo.ThermometerDataHistory
     * @Description 根据温度仪id查询最新的温度仪历史数据
     */
    ThermometerDataHistory queryThermometerDataHistoryLatestById(@Param("id") Integer id, @Param("nowTimestamp") Long nowTimestamp);
}
