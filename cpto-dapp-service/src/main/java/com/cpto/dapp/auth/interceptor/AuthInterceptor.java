package com.cpto.dapp.auth.interceptor;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.enums.AdminRoleEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.AuthorizedException;
import com.cpto.dapp.repository.ManagerAdminRepository;
import com.cpto.dapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器
 *
 * @author sunli
 * @date 2018/12/07
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthManager authManager;

    @Autowired
    private ManagerAdminRepository managerAdminRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 在前处理中取得token和id信息，并进行权限检查
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        /* 1.过滤拦截条件 */
        // 只拦截方法的场合
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 只拦截有权限检查注解的场合
        Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);
        ManagerAuth managerAuth = ((HandlerMethod) handler).getMethod().getAnnotation(ManagerAuth.class);
        if (ObjectUtil.isEmpty(auth) && ObjectUtil.isEmpty(managerAuth)) {
            return true;
        }

        /* 2.取得相关信息 */
        // 取得token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (ObjectUtil.isEmpty(token)) {
            throw new AuthorizedException();
        }

        // 取得id
        Long id;
        try {
            id = AuthManager.getIdByToken(token);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new AuthorizedException();
        }
        if (ObjectUtil.isEmpty(id)) {
            throw new AuthorizedException();
        }

        /* 3.检查缓存的token是否一致 */
        String cacheToken = authManager.getToken(id);
        if (ObjectUtil.isEmpty(cacheToken) || ObjectUtil.notEquals(cacheToken, token)) {
            throw new AuthorizedException();
        }

        /* 4.检查是否被禁用 */
        if (ObjectUtil.isNotEmpty(auth)) {
            User user = userRepository.findNotNullById(id);
            if (ObjectUtil.notEquals(user.getStatus(), StatusEnum.VALID.getCode())) {
                throw new AuthorizedException();
            }
        }

        if (ObjectUtil.isNotEmpty(managerAuth)) {
            ManagerAdmin managerAdmin = managerAdminRepository.findNotNullById(id);
            if (ObjectUtil.notEquals(managerAdmin.getStatus(), StatusEnum.VALID.getCode())) {
                throw new AuthorizedException();
            }

            /* 5.检查可写权限（有权：系统管理员、超级管理员，无权：普通管理员） */
            if (ObjectUtil.equals(managerAuth.value(), AuthManager.WRITE)) {
                if (ObjectUtil.notEquals(managerAdmin.getRoleType(), AdminRoleEnum.ROOT.getCode()) &&
                        ObjectUtil.notEquals(managerAdmin.getRoleType(), AdminRoleEnum.SUPER_ADMIN.getCode())) {
                    throw new AuthorizedException();
                }
            }
        }

        /* 6.设置当前登录id */
        if (ObjectUtil.isNotEmpty(auth)) {
            request.setAttribute(AuthManager.USER_ID, id);
        } else if (ObjectUtil.isNotEmpty(managerAuth)) {
            request.setAttribute(AuthManager.ADMIN_ID, id);
        }

        return true;
    }
}