package com.codej.web.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codej.web.Intercepts.SqlStatementInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@ConditionalOnExpression("'${spring.datasource4th.enabled:true}' == 'true'")
@MapperScan(value="com.codej.web.mapper.db4th", basePackages = "${spring.datasource4th.mapper-packages:}", sqlSessionFactoryRef="db4thSqlSessionFactory")
public class DatabaseConfiguration4th {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.datasource4th.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource4th.url}")
	private String url;
	@Value("${spring.datasource4th.username}")
	private String username;
	@Value("${spring.datasource4th.password}")
	private String password;
	@Value("${spring.mybatis.mapper-locations:classpath:/mapper/db4th/**/*.xml}")
	private String mapperLocations;
	@Value("${spring.mybatis.configuration.mapUnderscoreToCamelCase:false}")
	private Boolean mapUnderscoreToCamelCase;
	@Value("${spring.mybatis.configuration.cacheEnabled:false}")
	private Boolean cacheEnabled;
	@Value("${spring.mybatis.configuration.callSettersOnNulls:true}")
    private Boolean callSettersOnNulls;
    @Value("${spring.datasource4th.hikari.connectionTimeout:600000}")
	private long connectionTimeout;
	@Value("${spring.datasource4th.hikari.maximumPoolSize:20}")
	private int maximumPoolSize;
	@Value("${spring.datasource4th.hikari.maxLifetime:600000}")
	private long maxLifetime;
	@Value("${spring.datasource4th.hikari.minimumIdle:10}")
	private int minimumIdle;
	@Value("${spring.datasource4th.hikari.validationTimeout:30000}")
	private long validationTimeout;
	@Value("${spring.datasource4th.hikari.idleTimeout:300000}")
	private long idleTimeout;
    @Value("${spring.jeus.enabled:false}")
	private String jndiEnable;
    @Value("${spring.datasource4th.data-source-name:db4thDataSource}")
	private String dataSourceName;

    private JndiDataSourceLookup lookup = new JndiDataSourceLookup();

    @Bean(name = "db4thInterceptor")
    public SqlStatementInterceptor interceptor() {
        return new SqlStatementInterceptor();
    }
    @Bean(name = "db4thDataSource")
    public DataSource getDataSource() throws IOException{
        // String ip = System.getProperty("server.ip");
        // String url = "jdbc:postgresql://"+ip+":5432/capstonedb";
        if (jndiEnable.equals("false")) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(url);
            config.setMaximumPoolSize(maximumPoolSize);
            config.setIdleTimeout(idleTimeout);
            config.setConnectionTimeout(connectionTimeout);
            config.setMaxLifetime(maxLifetime);
            config.setMinimumIdle(minimumIdle);
            config.setValidationTimeout(validationTimeout);
            config.setUsername(username);
            config.setPassword(password);
            return new HikariDataSource(config);
        } else {
            return lookup.getDataSource(dataSourceName);
        }
    }

    @Bean(name = "db4thSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        configuration.setCacheEnabled(cacheEnabled);
        configuration.setJdbcTypeForNull(JdbcType.NULL);    //  jdbcType=VARCHAR 사용 없이 null 처리함.
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        configuration.setCallSettersOnNulls(callSettersOnNulls);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setPlugins(new Interceptor[] { interceptor() });
        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());   // for Transaction
        return sqlSessionFactoryBean.getObject();
    }

    // for Transaction
    @Bean
    public DataSourceTransactionManager transactionManager() throws IOException {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean(name = "db4thSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception{
        return new SqlSessionTemplate(getSqlSessionFactory());
    }
}
