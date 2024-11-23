package com.nhnacademy.springrestfinal.config;

import com.nhnacademy.springrestfinal.converter.Csv2MemberConverter;
import com.nhnacademy.springrestfinal.converter.Member2CsvConverter;
import com.nhnacademy.springrestfinal.resolver.PageableResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/member").setViewName("member");
        registry.addViewController("/google").setViewName("google");
        registry.addViewController("/403").setViewName("403");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageableResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new Member2CsvConverter());
        converters.add(new Csv2MemberConverter());
    }

}
