package com.nia.ip.sdn.linkage.config;

import com.nia.ip.sdn.linkage.vo.config.NiaDatabaseConfigVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(value="com.nia.ip.sdn.linkage.mapper.nia", sqlSessionFactoryRef="NiaSqlSessionFactory")
@EnableTransactionManagement
public class NiaDatabaseConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private NiaDatabaseConfigVo databaseConfigVo;

    @Bean(name = "NiaDataSource")
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

    @Bean(name = "NiaSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlmap/nia/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "NiaSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception{
        return new SqlSessionTemplate(getSqlSessionFactory());
    }

    // ✨ BATCH 전용 SqlSessionTemplate 추가 (이름을 다르게 지정해야 함)
    @Bean(name = "NiaBatchSqlSessionTemplate")
    public SqlSessionTemplate getBatchSqlSessionTemplate() throws Exception{
        // ExecutorType.BATCH를 지정하여 BATCH 모드로 동작하는 SqlSessionTemplate 생성
        return new SqlSessionTemplate(getSqlSessionFactory(), ExecutorType.BATCH);
    }
}
