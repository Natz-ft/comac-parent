package com.cmsr.comac.commander.service;

import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.pojo.UsersBean;
import com.cmsr.comac.commander.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZQ
 * @create 2020-04-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    UserService userService;

    /**
     * 测试用户登录
     */
    @Test
    public void loginByUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = new User();
        user.setUsername("Admin");
        user.setPassword("123456");
        ResultInfo resultInfo = userService.loginByUser(user, session);
        System.out.println(resultInfo.isFlag());

        System.out.println(userService);
    }

    /**
     * 测试查询用户列表
     */
    @Test
    public void queryUserList() {
        System.out.println(userService);
        ResultInfo resultInfo = userService.queryUserList();
        List<User> users = (List<User>) resultInfo.getData();
        HashMap map = new HashMap();
        map.put("1", users.toString());
        System.out.println(users);
        System.out.println("0000000000000000");
    }

    /**
     * 测试新增用户
     */
    @Test
    public void addUser() {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setPassword(Md5Util.stringToMd5("123456"));
        user.setUsername("user6");
        user.setRole("user");
        user.setCreatedAt(timeStamp);
        user.setDeleted(0);
        user.setNickname("用户6");
        ResultInfo resultInfo = userService.addUser(user);
        System.out.println("userId================================="+user.getId());
        System.out.println(resultInfo.getMsg());
    }

    /**
     * 测试更改用户信息
     */
    @Test
    public void updateUserInfo() {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setPassword("Aabb123456*");
        user.setUsername("user1");
        user.setRole("user");
        user.setCreatedAt(timeStamp);
        user.setDeleted(0);
        user.setNickname("user1");
        user.setValidateCode("shangfa");
        ResultInfo resultInfo = userService.updateUserInfo("user1", user);
        System.out.println(resultInfo.getMsg());
    }

    /**
     * 测试根据用户名删除用户
     */
    @Test
    public void deleteUserByUserName() {
        ResultInfo resultInfo = userService.deleteUserByUserName("zhangsan");
        System.out.println(resultInfo.getMsg());
    }

    /**
     * 测试用户登出
     */
    @Test
    public void logoutByUsername() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        ResultInfo resultInfo = userService.logoutByUsername("lisi", session);
        System.out.println(resultInfo.getMsg());
    }

    /**
     * 测试批量添加新用户
     */
    @Test
    public void testAddUserNotPressence() {
        List<UsersBean> usersBeans = new ArrayList<>();
        usersBeans.add(new UsersBean("CN=admin1,OU=技术管理团队,OU=工程技术中心,OU=中国航发商发制造,OU=acae,OU=ZZ,DC=acae,DC=com", "admin1", "管理员1", "admin", "admin1", "a", "a@qq.com", "a"));
        usersBeans.add(new UsersBean("CN=admin2,OU=技术管理团队,OU=工程技术中心,OU=中国航发商发制造,OU=acae,OU=ZZ,DC=acae,DC=com", "admin2", "管理员2", "admin", "admin2", "b", "b@qq.com", "b"));
        usersBeans.add(new UsersBean("CN=admin3,OU=技术管理团队,OU=工程技术中心,OU=中国航发商发制造,OU=acae,OU=ZZ,DC=acae,DC=com", "admin3", "管理员3", "admin", "admin3", "c", "c@qq.com", "c"));
        userService.addUserNotPressence(usersBeans);
    }

    /**
     * 测试批量更改用户信息
     */
    @Test
    public void testUpdateUserPressence() {
        List<UsersBean> usersBeans = new ArrayList<>();
        usersBeans.add(new UsersBean("user1", "u1", "a", "a", "a1", "a", "a@qq.com", "a"));
        usersBeans.add(new UsersBean("user2", "u2", "b", "b", "a2", "b", "b@qq.com", "b"));
        usersBeans.add(new UsersBean("user3", "u3", "c", "c", "a3", "c", "c@qq.com", "c"));
        userService.UpdateUserPressence(usersBeans);
    }

}