package com.kt.ipms.legacy.cmn.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigPropertieService {
	
	@Autowired
	private Properties configProp;
	
	public String getString(String key) {
		return configProp.getProperty(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(configProp.getProperty(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(configProp.getProperty(key));
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(configProp.getProperty(key));
	}

}
