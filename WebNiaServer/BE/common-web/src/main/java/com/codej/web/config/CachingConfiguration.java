package com.codej.web.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
@ConditionalOnExpression("'${spring.redis.enabled:true}' != 'true'")
public class CachingConfiguration {
    // @Bean
    // public CacheManager cacheManager() {
    //     SimpleCacheManager cacheManager = new SimpleCacheManager();
    //     cacheManager.setCaches(
    //             Arrays.asList(
    //                     new ConcurrentMapCache(CustomCacheKey.USER),
    //                     ));
    //     return cacheManager;
    // }


    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(100));
        return cacheManager;
    }
}
