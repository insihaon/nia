package com.nia.alarm.ip.simulator.vo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfigVo implements Serializable {  
	private static final long serialVersionUID = 1L;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.mybatis.mapper-locations}")
	private String mapperLocations;
	@Value("${spring.mybatis.mybatisConfig-locations}")
	private String mybatisConfiglocations;
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMapperLocations() {
		return mapperLocations;
	}
	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}
	public String getMybatisConfiglocations() {
		return mybatisConfiglocations;
	}
	public void setMybatisConfiglocations(String mybatisConfiglocations) {
		this.mybatisConfiglocations = mybatisConfiglocations;
	}
}
