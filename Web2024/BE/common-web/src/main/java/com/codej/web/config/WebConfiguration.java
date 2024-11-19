package com.codej.web.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codej.base.dto.AppDto;
import com.codej.base.property.FileStorageProperties;
import com.codej.base.utils.FileUtil;
import com.codej.web.Intercepts.AuthInterceptor;
import com.codej.web.Intercepts.IPAddressInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private IPAddressInterceptor ipAddressInterceptor;

    
	@Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private AppDto appDto;

    @Autowired
    public void configureJackson(ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*");
    }
        
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressInterceptor)
                .addPathPatterns("/**");

        // registry.addInterceptor(new LogInterceptor())
        //         .excludePathPatterns("/signin")
        //         .excludePathPatterns("/signout");

        // if (!appDto.isDevProfile() && appDto.getAuthUse() == true) {
            registry.addInterceptor(new AuthInterceptor())
                    .excludePathPatterns("/signin")
                    .excludePathPatterns("/signout");
        // }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        // RcaApiController 구현 시 한글 깨짐에 대한 조치. 압축 여부도 같이 체크 필요
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(new MediaType("text", "plain", StandardCharsets.UTF_8));
        list.add(new MediaType("application", "json", StandardCharsets.UTF_8));
        list.add(new MediaType("*", "*", StandardCharsets.UTF_8));
        converter.setSupportedMediaTypes(list);
        // converter.setWriteAcceptCharset(false);
        return converter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String url = fileStorageProperties.getAccessUrl();
        String dir = FileUtil.combine("file:", fileStorageProperties.getUploadDir(), "/");
		log.info("addResource: {} => {}", url, dir);

        // addResourceLocations 는 반드시 / 로 끝나야 한다.
        registry.addResourceHandler(url)
                .addResourceLocations(dir)
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());

        if(appDto.isDevProfile()) {
            registry.addResourceHandler("resources/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        }
        
        /*********************************************************************
         * [주의] application.yml 에 resources.static-locations 을 이용해 세팅하면 index.html을 못 찾아
         * 404 에러 발생함
         *********************************************************************/
    }
}
