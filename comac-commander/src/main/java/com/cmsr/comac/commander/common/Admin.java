package com.cmsr.comac.commander.common;

import java.lang.annotation.*;

/**
 * @author ztw
 * @data 2019/10/8 20:56
 * @description admin权限切面
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Admin {

}
