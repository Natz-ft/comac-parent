package com.cmsr.comac.commander.controller;

import com.cmsr.comac.commander.common.*;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.User;
import com.cmsr.comac.commander.service.ActionHistoryService;
import com.cmsr.comac.commander.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author ztw
 * @data 2019/8/19 15:56
 * @description 用户controller
 */

@Slf4j
@Api(value = "用户接口")
@RestController
@RequestMapping("/comac-commander/v2.0/user")
public class UserController {
    /**
     * 登录token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 用户业务
     */
    @Autowired
    private UserService userService;
    /**
     * 用户操作历史
     */
    @Autowired
    private ActionHistoryService actionHistoryService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param user     用户对象
     * @param response 响应
     * @param session  session
     * @return 用户
     */
    @ApiOperation(value = "用户登录", notes = "根据用户名和密码查询用户")
    @PostMapping("/{username}/auth")
    public ResponseData login(@PathVariable("username") String username, @RequestBody User user, HttpServletResponse response, HttpSession session) {
        //查询用户
        ResultInfo queryUserResult = userService.loginByUser(user, session);
        //异常
        if (!queryUserResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryUserResult.getMsg());
        }
        response.setHeader("x-auth-token", screenToken);
        //记录操作
        try {
            actionHistoryService.addActionHistory(username, "用户登录!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, queryUserResult.getData());
    }

    /**
     * 用户登出
     *
     * @param username 用户名
     * @param request  请求
     * @param response 响应
     * @param session  session
     * @return 登出
     */
    @ApiOperation(value = "用户登出", notes = "清除token")
    @DeleteMapping("/{username}/auth")
    public ResponseData loginOut(@PathVariable("username") String username, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "token错误!");
        } else {
            //登出成功,把请求头中的token清空
            response.setHeader("x-auth-token", null);

            //用户登出
            ResultInfo logoutByUsernameResult = userService.logoutByUsername(username, session);
            //异常
            if (!logoutByUsernameResult.isFlag()) {
                return ResponseData.out(CodeEnum.UNVERIFIED, logoutByUsernameResult.getMsg());
            }
            //记录操作
            try {
                actionHistoryService.addActionHistory(username, "用户登出!");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("记录操作异常!");
            }
            return ResponseData.out(CodeEnum.RETURN_URI, logoutByUsernameResult.getMsg());
        }
    }

    /**
     * 查询用户列表
     *
     * @param request 请求
     * @return 用户列表
     */
    @ApiOperation(value = "查询用户列表", notes = "权限为管理员")
    @GetMapping
    @Admin
    public ResponseData queryUserList(HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询用户列表
        ResultInfo queryUserListResult = userService.queryUserList();
        //异常
        if (!queryUserListResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, queryUserListResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, queryUserListResult.getData());
    }

    /**
     * 创建用户
     *
     * @param user    用户
     * @param request 请求
     * @return 用户
     */
    @ApiOperation(value = "创建用户", notes = "权限为管理员")
    @PostMapping
    @Admin
    public ResponseData addUser(@RequestBody User user, HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //新增用户
        ResultInfo addUserResult = userService.addUser(user);
        //异常
        if (!addUserResult.isFlag()) {
            return ResponseData.out(CodeEnum.UNVERIFIED, addUserResult.getMsg());
        }
        //记录操作
        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("user");
            actionHistoryService.addActionHistory(userSession.getUsername(), "创建" + user.getUsername() + "用户!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, addUserResult.getMsg());
    }

    /**
     * 修改用户信息
     *
     * @param username 用户名
     * @param user     用户
     * @param request  请求
     * @return 用户
     */
    @ApiOperation(value = "修改用户信息", notes = "权限为管理员admin")
    @PutMapping("/{username}")
    @Admin
    public ResponseData updateUserInfo(@PathVariable("username") String username, @RequestBody User user, HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        ResultInfo updateUserInfoResult = userService.updateUserInfo(username, user);
        if (!updateUserInfoResult.isFlag()) {
            return ResponseData.out(CodeEnum.UNVERIFIED, updateUserInfoResult.getMsg());
        }
        //记录操作
        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("user");
            actionHistoryService.addActionHistory(userSession.getUsername(), "修改" + username + "的用户信息!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, updateUserInfoResult.getMsg());

    }

    /**
     * 根据用户名删除用户
     *
     * @param username 用户名
     * @param request  请求
     * @return 用户
     */
    @ApiOperation(value = "根据用户名删除用户", notes = "权限为管理员")
    @DeleteMapping("/{username}")
    @Admin
    public ResponseData deleteUserByUserName(@PathVariable("username") String username, HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //删除用户
        ResultInfo deleteUserByUserNameResult = userService.deleteUserByUserName(username);
        //异常
        if (!deleteUserByUserNameResult.isFlag()) {
            return ResponseData.out(CodeEnum.UNVERIFIED, deleteUserByUserNameResult.getMsg());
        }
        //记录操作
        try {
            HttpSession session = request.getSession();
            User userSession = (User) session.getAttribute("user");
            actionHistoryService.addActionHistory(userSession.getUsername(), "删除" + username + "用户!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("记录操作异常!");
        }
        return ResponseData.out(CodeEnum.SUCCESSS, deleteUserByUserNameResult.getMsg());
    }

    /**
     * 返回用户操作记录
     *
     * @param username 用户名
     * @param request  请求
     * @return 用户操作记录
     */
    @ApiOperation(value = "返回用户操作记录", notes = "权限为管理员")
    @GetMapping("/action-history")
    @Admin
    public ResponseData getUserOperatingHistory(@RequestParam("username") String username, HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("x-auth-token");
        if (!screenToken.equals(token)) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "无token或token错误!");
        }
        //查询用户操作记录
        ResultInfo actionHistoryByUsernameResult = actionHistoryService.queryActionHistoryByUsername(username);
        //异常
        if (!actionHistoryByUsernameResult.isFlag()) {
            return ResponseData.out(CodeEnum.NOT_FOUND, actionHistoryByUsernameResult.getMsg());
        }
        return ResponseData.out(CodeEnum.SUCCESSS, actionHistoryByUsernameResult.getData());
    }

}
