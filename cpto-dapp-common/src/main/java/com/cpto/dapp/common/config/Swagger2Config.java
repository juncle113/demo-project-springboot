package com.cpto.dapp.common.config;

import com.cpto.dapp.common.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置
 *
 * @author sunli
 * @date 2018/12/07
 */
@Configuration
public class Swagger2Config {

    /**
     * 创建接口
     *
     * @return 接口信息
     */
    @Bean
    public Docket createRestApi() {

        /* 设置全局参数 */
        List<Parameter> parameterList = new ArrayList<Parameter>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder
                .name("Accept-Language")
                .description("语言区分")
                .modelRef(new ModelRef("string"))
                .defaultValue("zh")
                .parameterType("header")
                .required(false)
                .build();
        parameterList.add(parameterBuilder.build());

        parameterBuilder = new ParameterBuilder();
        parameterBuilder
                .name(Constant.HEADER_PAY_PASSWORD)
                .description("支付密码")
                .modelRef(new ModelRef("string"))
                .defaultValue("666666")
                .parameterType("header")
                .required(false)
                .build();
        parameterList.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cpto.dapp"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList)
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DApp")
                .description("API 文档")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
}