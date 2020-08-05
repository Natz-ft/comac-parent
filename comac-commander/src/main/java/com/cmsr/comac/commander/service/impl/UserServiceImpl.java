package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.UserRoleEnum;
import com.cmsr.comac.commander.dao.UserDao;
import com.cmsr.comac.commander.pojo.ResponseVO;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.pojo.UsersBean;
import com.cmsr.comac.commander.util.IpAddressUtil;
import com.cmsr.comac.commander.util.Md5Util;
import com.cmsr.comac.commander.service.UserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sun.util.resources.es.TimeZoneNames_es;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ztw
 * @data 2019/8/19 16:18
 * @description 用户实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 密码正则匹配
     */
    private static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,12}$";

    /**
     * 用户名正则匹配
     */
    private static final String USERNAEM_PATTERN = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$";

    /**
     * 校验码
     */
    @Value("${validate_code}")
    private String validateCode;
    /**
     * 用户dao
     */
    @Autowired
    private UserDao userDao;

    /**
     * 从商发域中获取用户列表
     */
    @Override
    public ResultInfo getUsers() {
        //获取用户列表
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("api_key", "317cc1a0-bd4d-3333-8952-000c29ac665a");
        HttpEntity<String> entity = new HttpEntity<>("params", headers);
        ResponseEntity<ResponseVO> responseEntity = new RestTemplate().exchange("http://10.43.0.90:8000/aecc/ldap/get_user_list/", HttpMethod.GET, entity, ResponseVO.class);
        ResponseVO responseVO = responseEntity.getBody();
        if (responseVO.is_success()) {
            return new ResultInfo(true, responseVO, "获取成功");
        }
        return new ResultInfo(false, null, "获取失败");
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public ResultInfo queryUserByUserName(String userName) {
        User user = userDao.queryUserByUserName(userName);
        if (user != null) {
            return new ResultInfo(true, user, "查询用户信息成功");
        }
        return new ResultInfo(false, null, "用户不存在");
    }

    /**
     * 用户登录
     *
     * @param user 用户
     * @return 用户
     */
    @Override
    public ResultInfo loginByUser(User user, HttpSession session) {
        //从数据库查询完整的登录名
        User u = userDao.queryUserByUserName(user.getUsername());
        if (u == null) {
            log.info("用户名或密码错误");
        }
        //用户登录
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //设置请求头
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("api_key", "317cc1a0-bd4d-3333-8952-000c29ac665a");
        //设置请求体
        Map<String, String> params = new HashMap<>();
        params.put("user", u.getDistinguishedName());
        params.put("password", user.getPassword());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange("http://10.0.43.93:8000/hf_paas/ldap/login", HttpMethod.POST, entity, JSONObject.class);
        JSONObject body = exchange.getBody();
        boolean is_success = false;
        try {
            is_success = (boolean) body.get("is_success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (is_success) {
            log.info("{}{}", "登录成功,用户名:", user.getUsername());
            HashMap<String, Object> map = Maps.newHashMap();
            session.setAttribute("user", u);
            map.put("expires", -1);
            map.put("nickname", u.getNickname());
            map.put("role", u.getRole());

            log.info("{}{}", "登录成功,用户名:", user.getUsername());
            return new ResultInfo(true, map);
        } else {
            log.info("用户名或密码错误!");
            return new ResultInfo(false, "用户名或密码错误!");
        }

       /*  //密码加密
       String toMd5 = Md5Util.stringToMd5(user.getPassword());
        //查询用户
        User queryUser = userDao.queryUser(user.getUsername(), toMd5);

        if (null == queryUser) {
            log.info("用户名或密码错误!");
            Integer times = (Integer) session.getAttribute("times");
            log.info("登录次数={}", times);
            if (times != null) {
                if (times == 5) {
                    return new ResultInfo(false, "请60秒后重试!");
                }
                times += 1;
                session.setAttribute("times", times);
            } else {
                session.setAttribute("times", 1);
            }
            times = (Integer) session.getAttribute("times");
            return new ResultInfo(false, "用户名或密码错误!");
        }
        HashMap<String, Object> map = Maps.newHashMap();

        session.setAttribute("user", queryUser);

        map.put("expires", -1);
        map.put("nickname", queryUser.getNickname());
        map.put("role", queryUser.getRole());

        log.info("{}{}", "登录成功,用户名:", user.getUsername());
        return new ResultInfo(true, map);*/
    }

    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    @Override
    public ResultInfo queryUserList() {
        List<User> userList = userDao.queryUserList();
        if (CollectionUtils.isEmpty(userList)) {
            return new ResultInfo(false, "未获取到数据!");
        }
        return new ResultInfo(true, userList);
    }

    /**
     * 新增用户
     *
     * @param user 用户
     */
    @Override
    public ResultInfo addUser(User user) {

        //用户名匹配规则
        if (!user.getUsername().matches(USERNAEM_PATTERN)) {
            log.info("用户名格式不合规!");
            return new ResultInfo(false, "用户名仅可以是英⽂字⺟、下划线和数字组成");
        }

        //查询用户名
        User userByUserName = userDao.queryUserByUserName(user.getUsername());
        if (null != userByUserName) {
            return new ResultInfo(false, "用户名已存在!");
        }

        //用户昵称长度匹配
        byte[] bytes = user.getNickname().getBytes();
        if (bytes.length > 32) {
            log.info("用户昵称定义格式不合规!");
            return new ResultInfo(false, "用户昵称设置过长!");
        }

        //密码匹配规则
        if (!user.getPassword().matches(PW_PATTERN)) {
            log.info("密码定义格式不合规!");
            return new ResultInfo(false, "密码必须包含大小写字母,数字和特殊符号,长度8-12位!");
        }

        //权限不能是admin
        if (UserRoleEnum.ADMIN.getRole().equals(user.getRole())) {
            return new ResultInfo(false, "不允许设置管理员权限");
        }

        //只能设置director和user权限
        if (UserRoleEnum.DIRECTOR.getRole().equals(user.getRole()) || UserRoleEnum.USER.getRole().equals(user.getRole())) {
            try {
                String passwordToMd5 = Md5Util.stringToMd5(user.getPassword());
                user.setPassword(passwordToMd5);
                userDao.addUser(user);
                return new ResultInfo(true, "创建用户成功！");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "创建失败！");
            }
        }
        return new ResultInfo(false, "权限设置有误!");
    }

    /**
     * 更新用户信息
     *
     * @param user     用户
     * @param username 用户名
     */
    @Override
    public ResultInfo updateUserInfo(String username, User user) {
        //校验码检查
        if (!validateCode.equals(user.getValidateCode())) {
            log.info("校验码错误!");
            return new ResultInfo(false, "校验码错误!");
        }

        //权限不能是admin
        if (UserRoleEnum.ADMIN.getRole().equals(user.getRole())) {
            return new ResultInfo(false, "不允许修改成管理员权限!");
        }
        //权限只能赋值为director或者user
        if (UserRoleEnum.DIRECTOR.getRole().equals(user.getRole()) || UserRoleEnum.USER.getRole().equals(user.getRole())) {
            try {
                if (StringUtils.isNotEmpty(user.getPassword())) {
                    //密码匹配规则
                    if (!user.getPassword().matches(PW_PATTERN)) {
                        log.info("密码定义格式不合规!");
                        return new ResultInfo(false, "密码必须包含大小写字母,数字和特殊符号,长度8-12位!");
                    }
                    String toMd5 = Md5Util.stringToMd5(user.getPassword());
                    user.setPassword(toMd5);
                }
                user.setUsername(username);
                userDao.updateUserInfo(user);
                return new ResultInfo(true, "更新成功!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "更新失败!");
            }
        }
        return new ResultInfo(false, "权限设置有误!");
    }

    /**
     * 根据用户名删除用户
     *
     * @param username 用户名
     */
    @Override
    public ResultInfo deleteUserByUserName(String username) {
        //先查询用户
        User user = userDao.queryUserByUserName(username);
        if (null == user) {
            return new ResultInfo(false, "无此用户!");
        }
        //不能删除管理员
        if (UserRoleEnum.ADMIN.getRole().equals(user.getRole())) {
            return new ResultInfo(false, "不能删除管理员!");
        }
        //删除用户
        try {
            userDao.deleteUserByUserName(username);
            return new ResultInfo(true, "成功删除用户:" + username);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false, "删除失败!");
        }
    }


    /**
     * 用户登出
     *
     * @param username 用户名
     * @param session  session
     * @return 用户
     */
    @Override
    public ResultInfo logoutByUsername(String username, HttpSession session) {
        User userSession = (User) session.getAttribute("user");
        if (null == userSession) {
            return new ResultInfo(false, "用户已登出!");
        }
        String userSessionUsername = userSession.getUsername();
        if (userSessionUsername.equals(username)) {
            session.removeAttribute("user");
            log.info("登出成功:" + userSessionUsername);
            return new ResultInfo(true, "登出成功!");
        }
        log.info("用户名不匹配!");
        return new ResultInfo(false, "登出失败!");
    }

    /**
     * 批量添加新用户
     *
     * @param users
     * @return
     */
    @Override
    public ResultInfo addUserNotPressence(List<UsersBean> users) {
        userDao.addUserNotPressence(users);
        return new ResultInfo(true, null, "批量添加新用户");
    }

    /**
     * 批量修改用户信息
     *
     * @param users
     * @return
     */
    @Override
    public ResultInfo UpdateUserPressence(List<UsersBean> users) {
        userDao.UpdateUserPressence(users);
        return new ResultInfo(true, null, "批量更新成功");


    }
}
