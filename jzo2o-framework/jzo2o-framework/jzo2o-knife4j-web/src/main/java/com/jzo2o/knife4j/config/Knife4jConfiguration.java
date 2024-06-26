package com.jzo2o.knife4j.config;

import com.jzo2o.common.model.Result;
import com.jzo2o.knife4j.filter.SwaggerFilter;
import com.jzo2o.knife4j.properties.SwaggerProperties;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
@Import(SwaggerFilter.class)
@ConditionalOnProperty(prefix = "swagger", name = "enable", havingValue = "true")
public class Knife4jConfiguration {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2(TypeResolver typeResolver) {
        // 1.初始化Docket
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        // 2.是否需要包装R
        docket.additionalModels(typeResolver.resolve(Result.class));
        return docket.apiInfo(new ApiInfoBuilder()
                        .title(this.swaggerProperties.getTitle())
                        .description(this.swaggerProperties.getDescription())
                        .contact(new Contact(
                                this.swaggerProperties.getContactName(),
                                this.swaggerProperties.getContactUrl(),
                                this.swaggerProperties.getContactEmail()))
                        .version(this.swaggerProperties.getVersion())
                        .build())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(this.swaggerProperties.getPackagePath()))
                .paths(PathSelectors.any())
                .build();
    }


}