package com.nia.alarm.preprocessor.vo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Getter
public class DatabaseConfigVo implements Serializable {  
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.data-source-class-name}")
	private String dataSourceClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.maximumPoolSize}")
	private int maximumPoolSize;
	@Value("${spring.datasource.idleTimeout}")
	private long idleTimeout;
	@Value("${spring.mybatis.mapper-locations}")
	private String mapperLocations;
	@Value("${spring.mybatis.mybatisConfig-locations}")
	private String mybatisConfiglocations;
}
