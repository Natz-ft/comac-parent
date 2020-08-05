package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.Vidicon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ZQ
 * @create 2020-07-08
 * @description
 */
@Mapper
public interface VidiconDao {
    /**
     * 查询网络摄像机列表
     *
     * @return
     */
    List<Vidicon> getVidiconList();
}
