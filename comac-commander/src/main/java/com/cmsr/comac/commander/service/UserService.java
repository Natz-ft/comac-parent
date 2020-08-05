package com.cmsr.comac.commander.service;


import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.pojo.UsersBean;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author ztw
 * @data 2019/8/19 16:17
 * @description 用户接口
 */

public interface UserService {

    /**
     * 查询用户
     *
     * @param user    用户
     * @param session session
     * @return 用户
     */
    ResultInfo loginByUser(User user, HttpSession session);

    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    ResultInfo queryUserList();

    /**
     * 创建用户
     *
     * @param user 用户
     * @return 用户
     */
    ResultInfo addUser(User user);

    /**
     * 更新用户信息
     *
     * @param username 用户名
     * @param user     用户
     * @return 用户
     */
    ResultInfo updateUserInfo(String username, User user);

    /**
     * 删除用户
     *
     * @param username 用户名
     * @return 用户
     */
    ResultInfo deleteUserByUserName(String username);

    /**
     * 用户登出
     *
     * @param username 用户名
     * @param session  session
     * @return result
     */
    ResultInfo logoutByUsername(String username, HttpSession session);

    /**
     * 获取用户列表
     * @return
     */
    ResultInfo getUsers();

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    ResultInfo queryUserByUserName(String userName);
    /**
     * 批量添加新用户
     *
     * @param users
     * @return
     */
    ResultInfo addUserNotPressence(List<UsersBean> users);

    /**
     * 批量更新用户
     *
     * @param users
     * @return
     */
    ResultInfo UpdateUserPressence(List<UsersBean> users);
}
