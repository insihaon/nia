package com.kt.ipms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codej.web.Intercepts.WebProxyInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.codej", "com.kt.ipms"}, exclude = { SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.codej", "com.kt.ipms", "com.kt.ipms.legacy.ipmgmt.allocmgmt.dao"})
@EnableCaching
@EnableWebSecurity
@EnableAsync
@EnableScheduling
public class IpmsApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Autowired
    private WebProxyInterceptor webProxyInterceptor;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(IpmsApplication.class);
	}

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webProxyInterceptor).addPathPatterns("/**");
    }

	public static void main(String[] args) {
		SpringApplication.run(IpmsApplication.class, args);
		log.info("\n\n>>> {} {} started <<<\n", IpmsApplication.class.getSimpleName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}
