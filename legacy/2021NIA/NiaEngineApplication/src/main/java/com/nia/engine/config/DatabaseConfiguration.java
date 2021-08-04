package com.nia.engine.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nia.engine.vo.DatabaseConfigVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages= {"com.nia.engine"})
public class DatabaseConfiguration {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private DatabaseConfigVo databaseConfigVo;

    @Bean
    public DataSource getDataSource() throws IOException{
        //	Properties prop = new Properties();
        HikariConfig config = new HikariConfig();
        //config.setDataSourceClassName(databaseConfigVo.getDataSourceClassName());
        config.setDriverClassName(databaseConfigVo.getDriverClassName());
        config.setJdbcUrl(databaseConfigVo.getUrl());
        config.setMaximumPoolSize(databaseConfigVo.getMaximumPoolSize());
        config.setIdleTimeout(databaseConfigVo.getIdleTimeout());
        config.setUsername(databaseConfigVo.getUsername());
        config.setPassword(databaseConfigVo.getPassword());
        //prop.load(DataSourceFactory.class.getClassLoader().getResourceAsStream("pgsql.properties"));
        //return new HikariDataSource(new HikariConfig(prop));
        return new HikariDataSource(config);
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlmap/*.xml"));
        //	sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception{
        return new SqlSessionTemplate(getSqlSessionFactory());
    }
}
