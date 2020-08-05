package com.cmsr.comac.commander.common;

import com.cmsr.comac.commander.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author ztw
 */
@Component
@Aspect
@Slf4j
public class RoleAspect {

    /**
     * admin权限切面
     */
    @Pointcut("@annotation(Admin)")
    public void admin() {

    }

    /**
     * director权限切面
     */
    @Pointcut("@annotation(Director)")
    public void director() {

    }

    /**
     * admin
     *
     * @param joinPoint 参数
     * @return 返回
     */
    @Around("admin()")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        User userSession = (User) session.getAttribute("user");
        if (null == userSession) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "未登录,请先登录!");
        }
        if (UserRoleEnum.ADMIN.getRole().equals(userSession.getRole())) {
            try {
                log.info("用户:" + userSession.getUsername() + ",访问:" + methodName + "方法");
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                return null;
            }
        }
        log.info("无权限访问!");
        return ResponseData.out(CodeEnum.UNVERIFIED, "无权限访问!");
    }

    /**
     * director权限
     *
     * @param joinPoint 参数
     * @return 返回
     */
    @Around("director()")
    public Object checkDirector(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        User userSession = (User) session.getAttribute("user");
        if (null == userSession) {
            return ResponseData.out(CodeEnum.UNVERIFIED, "未登录,请先登录!");
        }

        if (UserRoleEnum.ADMIN.getRole().equals(userSession.getRole()) || UserRoleEnum.DIRECTOR.getRole().equals(userSession.getRole())) {
            try {
                log.info("用户:" + userSession.getUsername() + ",访问:" + methodName + "方法");
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                return null;
            }
        }
        log.info("无权限访问!");
        return ResponseData.out(CodeEnum.UNVERIFIED, "无权限访问!");
    }

}
