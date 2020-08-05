package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.ThermometerImageHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/10 17:40
 * @description 温度仪图片历史数据
 */

@Mapper
public interface ThermometerImageHistoryDao {
    /**
     * 新增数字温度显示仪图片历史数据
     *
     * @param thermometerImageHistory 温度仪图片历史数据
     */
    void addThermometerImageHistory(@Param("thermometerImageHistory") ThermometerImageHistory thermometerImageHistory);

    /**
     * 查询过期温度仪图片历史数据
     *
     * @param timestamp 时间戳
     * @return 过期温度仪图片历史数据
     */
    List<ThermometerImageHistory> queryThermometerImageHistory(@Param("timestamp") long timestamp);

    /**
     * 清理周期之外的数字温度显示仪数据
     *
     * @param timestamp 时间戳
     */
    void deleteThermometerImageHistory(@Param("timestamp") long timestamp);

    /**
     * 查询数字温度显示仪图片历史数据
     *
     * @param id           温度仪id
     * @param nowTimestamp 当前时间戳
     * @return 数字温度显示仪图片历史数据
     */
    ThermometerImageHistory queryThermometerImageHistoryByThermometerId(@Param("id") Integer id, @Param("nowTimestamp") long nowTimestamp);

    /**
     * 备份周期之外的数字温度显示仪
     *
     * @param timestamp 时间戳
     */
    void copyThermometerImageHistory(@Param("timestamp") Long timestamp);
}
