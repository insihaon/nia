package com.nia.ai.performance.send.vo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
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
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getDataSourceClassName() {
		return dataSourceClassName;
	}
	public void setDataSourceClassName(String dataSourceClassName) {
		this.dataSourceClassName = dataSourceClassName;
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
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
	public long getIdleTimeout() {
		return idleTimeout;
	}
	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
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
