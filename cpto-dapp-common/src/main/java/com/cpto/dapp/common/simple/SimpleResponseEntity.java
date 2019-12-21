package com.cpto.dapp.common.simple;

import org.springframework.http.ResponseEntity;

/**
 * 封装ResponseEntity
 *
 * @author sunli
 * @date 2019/03/25
 */
public class SimpleResponseEntity<T> {

    /**
     * 返回get请求的响应结果
     *
     * @param body 响应内容
     * @return 响应结果
     */
    public static <T> ResponseEntity<T> get(T body) {
        return ResponseEntity.ok(body);
    }

    /**
     * 返回post请求的响应结果
     *
     * @return 响应结果
     */
    public static ResponseEntity post() {
        return ResponseEntity.created(null).build();
    }

    /**
     * 返回post请求的响应结果
     *
     * @param body 响应内容
     * @return 响应结果
     */
    public static <T> ResponseEntity<T> post(T body) {
        return ResponseEntity.created(null).body(body);
    }

    /**
     * 返回put请求的响应结果
     *
     * @return 响应结果
     */
    public static ResponseEntity put() {
        return ResponseEntity.ok().build();
    }

    /**
     * 返回delete请求的响应结果
     *
     * @return 响应结果
     */
    public static ResponseEntity delete() {
        return ResponseEntity.noContent().build();
    }
}