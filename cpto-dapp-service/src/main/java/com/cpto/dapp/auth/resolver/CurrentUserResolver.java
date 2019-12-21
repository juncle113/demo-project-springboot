package com.cpto.dapp.auth.resolver;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 当前用户id解析器
 *
 * @author sunli
 * @date 2018/12/07
 */
@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 过滤解析条件
        if (parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 将请求参数中的id设置到方法参数中
        Long userId = (Long) webRequest.getAttribute(AuthManager.USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (ObjectUtil.isEmpty(userId)) {
            throw new MissingServletRequestPartException(AuthManager.USER_ID);
        }
        return userService.getUser(userId);
    }
}
