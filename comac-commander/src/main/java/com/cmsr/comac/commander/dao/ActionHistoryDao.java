package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.ActionHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ztw
 * @data 2019/9/30 16:31
 * @description 用户操作历史dao
 */

@Mapper
public interface ActionHistoryDao {

    /**
     * 返回用户操作记录
     *
     * @param username 用户名
     * @return 用户操作记录
     */
    List<ActionHistory> queryActionHistoryByUsername(@Param("username") String username);

    /**
     * 新增人员操作记录
     *
     * @param actionHistory 用户操作历史
     */
    void addActionHistory(@Param("actionHistory") ActionHistory actionHistory);
}
