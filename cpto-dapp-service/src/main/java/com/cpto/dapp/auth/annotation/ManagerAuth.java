package com.cpto.dapp.auth.annotation;


import com.cpto.dapp.auth.AuthManager;

import java.lang.annotation.*;

/**
 * 描述方法上的鉴权属性
 *
 * @author sunli
 * @date 2018/12/07
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagerAuth {

    /**
     * 检查权限类型
     */
    int value() default AuthManager.READ;
}
