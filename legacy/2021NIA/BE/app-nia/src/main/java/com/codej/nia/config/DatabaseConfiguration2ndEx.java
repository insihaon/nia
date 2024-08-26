package com.codej.nia.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codej.web.config.DatabaseConfiguration2nd;

@Configuration
@EnableTransactionManagement
@ConditionalOnExpression("'${spring.datasource2nd.enabled:true}' == 'true'")
@MapperScan(basePackages = "${spring.datasource2nd.mapper-packages:}", sqlSessionFactoryRef="db2ndSqlSessionFactory")
// @MapperScan(basePackages = {"com.codej.nia.mapper.db2nd", "com.codej.nia.test.dao"}, sqlSessionFactoryRef="db2ndSqlSessionFactory")
public class DatabaseConfiguration2ndEx extends DatabaseConfiguration2nd{

    public DatabaseConfiguration2ndEx() {
        int a = 1;
    }

}
