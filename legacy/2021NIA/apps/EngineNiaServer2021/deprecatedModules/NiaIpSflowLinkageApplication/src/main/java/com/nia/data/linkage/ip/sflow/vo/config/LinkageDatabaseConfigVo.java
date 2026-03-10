package com.nia.data.linkage.ip.sflow.vo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Getter
public class LinkageDatabaseConfigVo implements Serializable {
	
	@Value("${spring.datasource.linkage.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.linkage.url}")
	private String url;
	@Value("${spring.datasource.linkage.username}")
	private String username;
	@Value("${spring.datasource.linkage.password}")
	private String password;
	@Value("${spring.datasource.linkage.maximumPoolSize}")
	private int maximumPoolSize;
	@Value("${spring.datasource.linkage.idleTimeout}")
	private long idleTimeout;
	@Value("${spring.mybatis.mapper-locations}")
	private String mapperLocations;
	@Value("${spring.mybatis.mybatisConfig-locations}")
	private String mybatisConfiglocations;
}
