package com.kt.ipms.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codej.web.config.DatabaseConfiguration2nd;
import com.kt.ipms.legacy.cmn.typehandler.CIDRTypeHandler;

@Configuration
@EnableTransactionManagement
@ConditionalOnExpression("'${spring.datasource2nd.enabled:true}' == 'true'")
@MapperScan(value="com.kt.ipms.mapper.db2nd", basePackages = "${spring.datasource2nd.mapper-packages:}", sqlSessionFactoryRef="db2ndSqlSessionFactory")
public class DatabaseConfiguration2ndEx extends DatabaseConfiguration2nd{

    @Override
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        SqlSessionFactory sqlSessionFactory = super.getSqlSessionFactory();
        if (sqlSessionFactory != null) {
            org.apache.ibatis.session.Configuration config = sqlSessionFactory.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = config.getTypeHandlerRegistry();
    
            typeHandlerRegistry.register(Object.class, new CIDRTypeHandler());
            // typeHandlerRegistry.register(BigInteger.class, new BigIntTypeHandler());
        }
        return sqlSessionFactory;
    }
}
