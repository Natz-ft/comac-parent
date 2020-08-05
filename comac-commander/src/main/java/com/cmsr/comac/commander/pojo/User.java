package com.cmsr.comac.commander.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ztw
 * @data 2019/8/19 15:57
 * @description 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户id
     */
    private int id;


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户角色  可以是admin、director和user
     */
    private String role;
    /**
     * 登录用户名
     */
    private String distinguishedName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updatedAt;

    /**
     * 0表示正常，1表示删除
     */
    private Integer deleted;

    /**
     * 校验码
     */
    @JsonProperty("validate_code")
    private String validateCode;
//    /**
//     * 是否登录true代表登录，
//     */
//    private boolean loginStatus;
}
