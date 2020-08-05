package com.cmsr.comac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collections;


/**
 * @author ztw
 * @data 2019/8/17 16:14
 * @description 启动类
 */

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class CommanderApplication extends SpringBootServletInitializer {

    /**
     * restTemplate
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(headerInterceptor()));
        return restTemplate;
    }

    /**
     * @return ClientHttpRequestInterceptor
     */
    @Bean
    public ClientHttpRequestInterceptor headerInterceptor() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                String token = attributes.getRequest().getHeader("x-auth-token");
                request.getHeaders().add("x-auth-token", token);
                return execution.execute(request, body);
            }
        };
    }

    /**
     * 启动类
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CommanderApplication.class, args);
    }

    /**
     * @param builder builder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.headless(false);
        return builder.sources(CommanderApplication.class);
    }
}

