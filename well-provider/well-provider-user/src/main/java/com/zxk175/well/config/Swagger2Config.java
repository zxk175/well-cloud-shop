package com.zxk175.well.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * swagger2配置
 *
 * @author zxk175
 * @since 2019/03/23 16:06
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createAppApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 禁用默认的响应
                .useDefaultResponseMessages(false)
                // 设置为true，以使文档代码生成友好
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("测试APIs项目")
                .description("了解更多点击：https://zxk175.com")
                .termsOfServiceUrl("https://zxk175.com")
                .contact(new Contact("张小康", "https://zxk175.com", "zxk175@qq.com"))
                .version("1.0.0")
                .build();
    }

    private List<ApiKey> security() {
        return Lists.newArrayList(
                new ApiKey("token", "token", "header")
        );
    }
}
