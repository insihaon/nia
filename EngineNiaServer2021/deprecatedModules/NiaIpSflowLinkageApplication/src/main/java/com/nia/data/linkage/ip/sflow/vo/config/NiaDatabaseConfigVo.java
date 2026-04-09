package com.nia.data.linkage.ip.sflow.vo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Getter
public class NiaDatabaseConfigVo implements Serializable {
	
	@Value("${spring.datasource.nia.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.nia.url}")
	private String url;
	@Value("${spring.datasource.nia.username}")
	private String username;
	@Value("${spring.datasource.nia.password}")
	private String password;
	@Value("${spring.datasource.nia.maximumPoolSize}")
	private int maximumPoolSize;
	@Value("${spring.datasource.nia.idleTimeout}")
	private long idleTimeout;
	@Value("${spring.mybatis.mapper-locations}")
	private String mapperLocations;
	@Value("${spring.mybatis.mybatisConfig-locations}")
	private String mybatisConfiglocations;
}
