package com.hifive.bururung.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<ErrorPageFilter> disableDefaultErrorPageFilter(ErrorPageFilter filter) {
        FilterRegistrationBean<ErrorPageFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setEnabled(false);
        return bean;
    }
}
