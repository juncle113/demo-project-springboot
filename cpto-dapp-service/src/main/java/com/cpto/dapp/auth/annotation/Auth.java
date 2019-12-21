package com.cpto.dapp.auth.annotation;

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
public @interface Auth {

}
