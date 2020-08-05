package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZQ
 * @create 2020-07-02
 * @description 摄像机实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vidicon {
    private Integer id;
    private String ip;
    private String description;
    private Integer labID;
    private String targetDevice;
}
