package com.codej.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
// @SpringBootApplication(scanBasePackages = {"com.codej"}, exclude = { SecurityAutoConfiguration.class })
@EnableCaching
@EnableWebSecurity
@EnableAsync
@EnableScheduling

public class WebApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
		log.info("\n\n>>> {} {} started <<<\n", WebApplication.class.getSimpleName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
