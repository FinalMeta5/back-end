package com.hifive.bururung.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ErrorPageFilter customErrorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean<ErrorPageFilter> disableErrorPageFilter(ErrorPageFilter customErrorPageFilter) {
        FilterRegistrationBean<ErrorPageFilter> bean = new FilterRegistrationBean<>(customErrorPageFilter);
        bean.setEnabled(false);
        return bean;
    }
}
