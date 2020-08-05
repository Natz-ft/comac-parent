package com.cmsr.comac.commander.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ztw
 * @data 2019/10/18 11:09
 * @description testLog框坐标封装
 */

@Component
@ConfigurationProperties(prefix = "pop")
public class Pop {
    /**
     * testLog弹出坐标
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
     * @param maps 坐标
     */
    public void setMaps(Map<String, Integer> maps) {
        this.maps = maps;
    }
}
