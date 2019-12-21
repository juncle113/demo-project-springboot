package com.cpto.dapp.auth.annotation;

import java.lang.annotation.*;

/**
 * 标注方法里需要填充的管理员信息
 *
 * @author sunli
 * @date 2019/04/06
 */
@Inherited
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentAdmin {

}
