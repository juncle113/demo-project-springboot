package com.cpto.dapp.auth.resolver;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.service.ManagerAdminService;
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
 * 管理员信息解析器
 *
 * @author sunli
 * @date 2019/04/06
 */
@Component
public class CurrentAdminResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private ManagerAdminService managerAdminService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 过滤解析条件
        if (parameter.hasParameterAnnotation(CurrentAdmin.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 将请求参数中的id设置到方法参数中
        Long adminId = (Long) webRequest.getAttribute(AuthManager.ADMIN_ID, RequestAttributes.SCOPE_REQUEST);
        if (ObjectUtil.isEmpty(adminId)) {
            throw new MissingServletRequestPartException(AuthManager.ADMIN_ID);
        }
        return managerAdminService.getAdmin(adminId);
    }
}
