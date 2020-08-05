package com.cmsr.comac.commander.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @data 2019/9/29 9:48
 * @description 用户操作历史实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionHistory implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户动作
     */
    private String action;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 0表示正常，1表示删除，默认值为0
     */
    private Integer deleted;

}
