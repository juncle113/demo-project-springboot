package com.cpto.dapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;

/**
 * 程序启动入口
 * 开启异步处理
 * 开启缓存SpringCache（ehcache实现）
 * 开启接口文档Swagger2
 * 开启EnableScheduling
 * 开启事务管理
 *
 * @author sunli
 * @date 2018/12/07
 */
@EnableAsync
@EnableCaching
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class Application {

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-8"));
        SpringApplication.run(Application.class, args);
    }
}