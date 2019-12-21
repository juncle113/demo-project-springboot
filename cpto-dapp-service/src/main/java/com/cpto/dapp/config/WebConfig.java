package com.cpto.dapp.config;


import com.cpto.dapp.auth.interceptor.AuthInterceptor;
import com.cpto.dapp.auth.interceptor.PayAuthInterceptor;
import com.cpto.dapp.auth.resolver.CurrentAdminResolver;
import com.cpto.dapp.auth.resolver.CurrentUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web配置
 *
 * @author sunli
 * @date 2019/01/30
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private CurrentUserResolver currentUserResolver;

    @Autowired
    private CurrentAdminResolver currentAdminResolver;

    @Autowired
    private PayAuthInterceptor payAuthInterceptor;

    /**
     * 加载拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加载鉴权
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        registry.addInterceptor(payAuthInterceptor).addPathPatterns("/**");
    }

    /**
     * 加载参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 加载当前用户信息
        argumentResolvers.add(currentUserResolver);
        // 加载当前管理员信息
        argumentResolvers.add(currentAdminResolver);
    }
}