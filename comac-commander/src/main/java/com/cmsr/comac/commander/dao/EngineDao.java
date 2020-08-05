package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Engine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/29 10:20
 * @description 油机dao
 */

@Mapper
public interface EngineDao {


    /**
     * 查询油机列表
     *
     * @param location 实验室
     * @return 油机列表
     */
    List<Engine> queryEngineList(@Param("location") String location);

    /**
     * 根据id查询油机对象
     *
     * @param id 油机id
     * @return 油机
     */
    Engine queryEngineById(@Param("id") Integer id);

    /**
     * 关闭油机功能
     *
     * @param id 油机id
     */
    void updateEngineEnableToOff(@Param("id") Integer id);

    /**
     * 关闭视觉识别
     *
     * @param id 油机id
     */
    void updateEngineRecognitionEnableToOff(@Param("id") Integer id);

    /**
     * 开启油机采集
     *
     * @param engineId 油机id
     */
    void updateEngineEnableToOn(Integer engineId);

    /**
     * 开启油机视觉识别
     *
     * @param engineId 油机id
     */
    void updateEngineRecognitionEnableToOn(Integer engineId);
}
