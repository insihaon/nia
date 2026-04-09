package com.codej.web.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching
@Configuration

// 조건에 만족 할 때만 EnableRedisHttpSession (Redis Session 기능) 활성화 시킴
@ConditionalOnExpression("'${spring.redis.enabled:true}' == 'true'")
@ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "redis")   
@EnableRedisHttpSession 
public class BaseRedisSessionConfiguration {
}