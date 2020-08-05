package com.cmsr.comac.commander.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/18 11:06
 * @description 红绿状态rgb坐标封装
 */

@Component
@ConfigurationProperties(prefix = "rg")
public class Rg {

    /**
     * 红绿状态rgb坐标
     */
    private Map<String, Integer> maps;

    /**
     * get
     *
     * @return maps
     */
    public Map<String, Integer> getMaps() {
        return maps;
    }

    /**
     * set
     *
     * @param maps rgb坐标
     */
    public void setMaps(Map<String, Integer> maps) {
        this.maps = maps;
    }
}
