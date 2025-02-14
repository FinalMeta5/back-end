package com.hifive.bururung.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.hifive.hiapp.commons.HttpMethodOverrideFilter;

import jakarta.servlet.Filter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Bean
//    FilterRegistrationBean<HttpMethodOverrideFilter> httpMethodOverrideFilter() {
//        FilterRegistrationBean<HttpMethodOverrideFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new HttpMethodOverrideFilter());
//        registrationBean.addUrlPatterns("/api/*"); // 필터가 적용될 URL 패턴
//        return registrationBean;
//    }

	@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<>(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 적용
                .allowedOrigins("http://localhost:3000", "https://hifive55.shop", "https://localhost:3000") // 허용할 Origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("accesstoken")
                .allowCredentials(true); // 인증 정보 허용
    }

}