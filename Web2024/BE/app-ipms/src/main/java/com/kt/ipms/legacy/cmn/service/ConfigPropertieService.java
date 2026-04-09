package com.kt.ipms.legacy.cmn.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import org.springframework.core.env.Environment;

@SpringBootApplication
@Component
public class ConfigPropertieService {

	@Configuration
	@PropertySource("classpath:properties/config.properties")
	public class DefaultConfig { }
	@Configuration
	@Profile("dev")
	@PropertySource("classpath:properties/config.dev")
	public class DevConfig { }
	@Configuration
	@Profile("test")
	@PropertySource("classpath:properties/config.test")
	public class TestConfig { }

	@Autowired
	private Environment env;

	public String getString(String key) {
		return env.getProperty(key);
	}
	

	public int getInt(String key) {
		return Integer.parseInt(env.getProperty(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(env.getProperty(key));
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(env.getProperty(key));
	}

}
