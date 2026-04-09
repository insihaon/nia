package com.codej.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codej.web.cached.RequestBodyFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> loggingFilter() {
        FilterRegistrationBean<RequestBodyFilter> registrationBean = new FilterRegistrationBean<>();

        // 필터 인스턴스 등록
        registrationBean.setFilter(new RequestBodyFilter());
        
        // 필터가 적용될 URL 패턴 설정
        registrationBean.addUrlPatterns("/*"); 
        
        // 필터의 우선순위 설정 (낮을수록 먼저 실행)
        registrationBean.setOrder(1); 

        return registrationBean;
    }
}