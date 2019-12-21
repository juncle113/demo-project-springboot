package com.cpto.dapp.auth.annotation;

import java.lang.annotation.*;

/**
 * 描述方法上的支付鉴权属性
 *
 * @author sunli
 * @date 2019/01/30
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayAuth {

}
