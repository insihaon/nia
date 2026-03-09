package com.nia.korenrca.shared;

import com.nia.korenrca.service.util.Utils;

import java.io.FileInputStream;
import java.util.Properties;


public enum DataSourceType {
    // @formatter:off
    DefaultDB("rcadb_datasource", Utils.getPropertiesFile()),
    RCADB("rcadb_datasource", Utils.getPropertiesFile()),
    ;
    // @formatter:on

    /*
     * MyBatis의 설정 파일의 Environment ID
     */
    private String environmentId;
    private Properties properties;
    private String url;

    private DataSourceType(String environmentId) {
        this.environmentId = environmentId;
        url = null;
    }

    private DataSourceType(String environmentId, String url) {
        this(environmentId);
        this.url = url;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public String getUrl() {
        return this.url;
    }

    public Properties getProperties() {
        if (properties == null) {
            try {
                properties = new Properties();
                properties.load(new FileInputStream(url));
            } catch (Exception e) {
                properties = null;
                e.printStackTrace();
            }
        }
        return properties;
    }
}
