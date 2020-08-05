package com.cmsr.comac.commander.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztw
 * @data 2019/8/22 15:29
 * @description
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //name表示名称，description表示描述
        ticketPar.name("x-auth-token").description("登录校验")
                .modelRef(new ModelRef("string")).parameterType("header")
                //required表示是否必填，defaultvalue表示默认值
                .required(false).defaultValue("token").build();
        //添加完此处一定要把下边的带***的也加上否则不生效
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                /* .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))*/
                .apis(RequestHandlerSelectors.basePackage("com.cmsr.comac.commander.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("商发开发接口文档")
                .description("")
                .termsOfServiceUrl("")
                .contact(new Contact("仲泰玮","","17621907619@163.com"))
                .version("1.0")
                .build();
    }
}
