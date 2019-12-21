package com.cpto.dapp.auth.interceptor;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.PayAuth;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.exception.AuthorizedException;
import com.cpto.dapp.exception.PayPasswordErrorException;
import com.cpto.dapp.exception.PayPasswordNotSetException;
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
 * @date 2019/01/30
 */
@Component
public class PayAuthInterceptor extends HandlerInterceptorAdapter {

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
        PayAuth payAuth = ((HandlerMethod) handler).getMethod().getAnnotation(PayAuth.class);
        if (ObjectUtil.isEmpty(payAuth)) {
            return true;
        }

        // 检查支付密码
        if (ObjectUtil.isNotEmpty(payAuth)) {

            /* 2.取得相关信息 */
            // 取得token
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (ObjectUtil.isEmpty(token)) {
                throw new AuthorizedException();
            }

            // 取得id
            Long id = AuthManager.getIdByToken(token);
            if (ObjectUtil.isEmpty(id)) {
                throw new AuthorizedException();
            }

            User user = userRepository.findNotNullById(id);
            if (ObjectUtil.isEmpty(user.getPayPassword())) {
                throw new PayPasswordNotSetException();
            }

            String payPassword = StringUtil.toMd5(request.getHeader(Constant.HEADER_PAY_PASSWORD));
            if (ObjectUtil.notEquals(payPassword, user.getPayPassword())) {
                throw new PayPasswordErrorException();
            }
        }

        return true;
    }
}