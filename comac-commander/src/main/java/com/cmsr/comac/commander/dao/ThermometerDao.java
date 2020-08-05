package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Thermometer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/29 14:44
 * @description 温度仪dao
 */

@Mapper
public interface ThermometerDao {

    /**
     * 数字温度仪表盘列表
     *
     * @param location 实验室
     * @return 温度仪列表
     */
    List<Thermometer> queryThermometerList(@Param("location") String location);

    /**
     * 根据id查询数字温度仪表盘对象
     *
     * @param id 温度仪id
     * @return 温度仪
     */
    Thermometer queryThermometerById(@Param("id") Integer id);

    /**
     * 关闭数字温度显示仪功能
     *
     * @param id 温度仪id
     */
    void updateThermometerEnableToOff(@Param("id") Integer id);

    /**
     * 开启数字温度仪功能
     *
     * @param thermometerId 温度仪id
     */
    void updateThermometerEnableToOn(Integer thermometerId);

    /**
     * 关闭视觉识别
     *
     * @param id 温度仪id
     */
    void updateEngineRecognitionEnableToOff(@Param("id") Integer id);


    /**
     * 开启数字温度仪视觉识别
     *
     * @param thermometerId 温度仪id
     */
    void updateEngineRecognitionEnableToOn(Integer thermometerId);
}
