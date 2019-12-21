package com.cpto.dapp.auth;

import com.cpto.dapp.common.util.StringUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 鉴权Manager
 *
 * @author sunli
 * @date 2018/12/07
 */
@Component
@CacheConfig(cacheNames = {AuthManager.TOKENS})
public class AuthManager {

    /**
     * 检查权限：只读权限
     */
    public static final int READ = 1;

    /**
     * 检查权限：可写权限
     */
    public static final int WRITE = 2;

    /**
     * token缓存名
     */
    public static final String TOKENS = "tokens";

    /**
     * 当前用户id
     */
    public static final String USER_ID = "userId";

    /**
     * 管理员id
     */
    public static final String ADMIN_ID = "adminId";

    /**
     * 生成token
     *
     * @return token
     */
    public static String generateToken(Long id) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        return StringUtil.encodeBase64(uuid.concat("_").concat(String.valueOf(id)));
    }

    /**
     * 根据token取得id
     *
     * @param token token
     * @return id
     * @throws IllegalArgumentException 无效参数异常
     */
    public static Long getIdByToken(String token) throws IllegalArgumentException {
        return Long.valueOf(StringUtil.decodeBase64(token).split("_")[1]);
    }

    /**
     * 缓存token
     *
     * @return token
     */
    @CachePut(key = "#id")
    public String createToken(Long id) {
        return generateToken(id);
    }

    /**
     * 清除缓存的token
     */
    @CacheEvict(key = "#id")
    public void removeToken(Long id) {
        return;
    }

    /**
     * 取得缓存的token
     *
     * @return token
     */
    @Cacheable(key = "#id")
    public String getToken(Long id) {
        return null;
    }
}
