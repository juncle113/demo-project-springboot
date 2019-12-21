package com.cpto.dapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The class JacksonUtil
 * <p>
 * json字符与对像转换
 *
 * @version: $Revision$ $Date$ $LastChangedBy$
 */
@Component
@Slf4j
public final class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public static HttpUtil httpUtil;
    
    @PostConstruct
    public void init() {
        httpUtil = this;
        httpUtil.restTemplate = this.restTemplate;
    }

    public static ResponseEntity<Map> send(String url, Object reqJsonStr, String authorization, HttpMethod method, MediaType mediaType) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        HttpEntity entity = new HttpEntity(reqJsonStr, headers);
        ResponseEntity<Map> resp = null;
        try {
            httpUtil.restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            resp = httpUtil.restTemplate.exchange(url, method, entity, Map.class);
            if (resp.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return resp;
            }
            if (!resp.getStatusCode().equals(HttpStatus.OK) && !resp.getStatusCode().equals(HttpStatus.CREATED)) {
                log.error("获取数据异常url:" + url + " node:" + resp.getStatusCode() + resp.getBody());
                resp = null;
            }
        } catch (Exception e) {
            log.error("服务器异常url:" + url);
            log.error("接口返回异常信息：" + e.getMessage());
        }

        return resp;
    }
}