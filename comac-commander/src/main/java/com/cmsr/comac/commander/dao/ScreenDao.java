package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Screen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/29 10:20
 * @description 截屏dao
 */

@Mapper
public interface ScreenDao {


    /**
     * 查询截屏列表
     *
     * @param location 实验室
     * @return 截屏
     */
    List<Screen> queryScreenList(@Param("location") String location);

    /**
     * 根据id查询截屏对象
     *
     * @param id 屏幕id
     * @return 截屏
     */
    Screen queryScreenById(@Param("id") Integer id);

    /**
     * 关闭指定id的屏幕截屏
     *
     * @param id 屏幕id
     */
    void updateScreenEnableToOff(@Param("id") Integer id);

    /**
     * 关闭视觉识别
     *
     * @param id 屏幕id
     */
    void updateScreenRecognitionEnableToOff(@Param("id") Integer id);

    /**
     * 开启截屏采集功能
     *
     * @param screenId 屏幕id
     */
    void updateScreenEnableToOn(Integer screenId);

    /**
     * 开启截屏视觉识别
     *
     * @param screenId 屏幕id
     */
    void updateScreenRecognitionEnableToOn(Integer screenId);
}
