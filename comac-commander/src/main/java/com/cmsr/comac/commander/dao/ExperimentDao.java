package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Experiment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/10/24 13:13
 * @description 实验总开关dao
 */

@Mapper
public interface ExperimentDao {

    /**
     * 查询实验开关列表
     *
     * @param location 实验室
     * @return 开关
     */
    List<Experiment> getExperimentActionList(@Param("location") String location);

    /**
     * 根据id查询实验开关
     *
     * @param id 开关id
     * @return 开关
     */
    Experiment getExperimentActionById(@Param("id") Integer id);

    /**
     * 关闭对应id的实验
     *
     * @param id 开关id
     */
    void updateEnableToOff(@Param("id") Integer id);

    /**
     * 开启对应id的实验
     *
     * @param id 开关id
     */
    void updateEnableToOn(@Param("id") Integer id);
}
