package com.cmsr.comac.commander.common;

/**
 * @author ztw
 * @data 2019/9/27 12:49
 * @description 用户角色枚举类
 */

public enum UserRoleEnum {

    /**
     * admin角色  具备所有权限
     */
    ADMIN("admin"),

    /**
     * director角色  具有开启/关闭等功能权限
     */
    DIRECTOR("director"),

    /**
     * user角色  只具备查询权限
     */
    USER("user");

    /**
     * 角色
     */
    private String role;

    /**
     * 构造
     */
    UserRoleEnum() {
    }

    /**
     * @param role 权限
     */
    UserRoleEnum(String role) {
        this.role = role;
    }

    /**
     * get
     *
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * set
     *
     * @param role 权限
     */
    public void setRole(String role) {
        this.role = role;
    }
}
