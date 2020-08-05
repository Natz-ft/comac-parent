package com.cmsr.comac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * @author ztw
 * @data 2019/8/17 16:07
 * @description
 */

@SpringBootApplication
public class ScoutApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ScoutApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.headless(false);
        return builder.sources(ScoutApplication.class);
    }
}
