package com.nia.rca.cluster.preprocessor.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
public class DatabaseConfigVo implements Serializable {
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
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
}
