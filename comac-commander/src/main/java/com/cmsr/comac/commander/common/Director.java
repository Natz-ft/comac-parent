package com.cmsr.comac.commander.common;


import java.lang.annotation.*;

/**
 * @author ztw
 * @data 2019/10/17 15:38
 * @description 自定义注解类, 用于aop切面进行权限管理(director权限管理)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Director {
}
