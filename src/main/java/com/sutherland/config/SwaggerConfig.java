package com.sutherland.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

/**
 * @author vinodh kumar thimmisetty
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    final static String TRADE_MARK = "\u2122";

    public static final Contact DEFAULT_CONTACT = new Contact(
            "Sutherland" + TRADE_MARK,
            "https://github.com/Vinodh-thimmisetty",
            "vinodh5052@gmail.com");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Sutherland Gaming Challenge " + TRADE_MARK,
            "Sutherland Gaming Challenge by HackerEarth " + TRADE_MARK,
            "v1",
            "Sutherland related terms" + TRADE_MARK,
            DEFAULT_CONTACT,
            "License details",
            "http://www.Sutherland.com/licenses",
            Collections.emptyList());

    @Bean
    public Docket gamingAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.sutherland.web"))
                .paths(PathSelectors.ant("/v1/*/**"))
                .build()
                .apiInfo(DEFAULT_API_INFO);
    }
}