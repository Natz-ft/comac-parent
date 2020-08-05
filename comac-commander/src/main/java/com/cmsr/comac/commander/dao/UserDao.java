package com.cmsr.comac.commander.dao;

import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.pojo.UsersBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ztw
 * @data 2019/8/19 20:31
 * @description 用户dao
 */
@Mapper
public interface UserDao {
    /**
     * 查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    User queryUser(@Param("username") String username, @Param("password") String password);

    /**
     * 查询用户列表
     *
     * @return List<User>
     */
    List<User> queryUserList();

    /**
     * 新增用户
     *
     * @param user 用户
     */
    void addUser(@Param("user") User user);

    /**
     * 更新用户信息
     *
     * @param user 用户
     */
    void updateUserInfo(@Param("user") User user);

    /**
     * 删除用户
     *
     * @param username 用户名
     */
    void deleteUserByUserName(@Param("username") String username);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return User
     */
    User queryUserByUserName(@Param("username") String username);

    /**
     * 批量添加新用户
     *
     * @param users
     */
    void addUserNotPressence(@Param("users") List<UsersBean> users);
    /**
     * 批量更新
     *
     * @param users
     */
    void UpdateUserPressence(@Param("users") List<UsersBean> users);
}
