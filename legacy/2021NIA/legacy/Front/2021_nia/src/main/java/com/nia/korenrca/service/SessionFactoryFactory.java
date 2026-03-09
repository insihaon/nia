package com.nia.korenrca.service;

import com.nia.korenrca.shared.DataSourceType;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SessionFactoryFactory {
    private static Logger logger = Logger.getLogger(SessionFactoryFactory.class.getName());

    private static final String CONFIG_RESOURCE = "com/nia/korenrca/server/SqlMapConfig.xml";
    public static final String ATTRIBUTE_NAME = "SessionFactoryFactory";
    public static final String HOST_NAME = "Host";

    private static final HashMap<String, SqlSessionFactory> sessionFactoryMap = new HashMap<String, SqlSessionFactory>();

    private final ArrayList<String[]> typeAliases;
    private final ArrayList<String> mapperResources;

    public SessionFactoryFactory(ArrayList<String[]> typeAliases, ArrayList<String> mapperResources) {
        super();

        this.typeAliases = typeAliases;
        this.mapperResources = mapperResources;
    }

    public SqlSessionFactory get(String dataSource) throws Exception {
        SqlSessionFactory sessionFactory = sessionFactoryMap.get(dataSource);
        return sessionFactory;
    }

    public void createSqlSessionFactories() {
        for (int i = 0; i < DataSourceType.values().length; i++) {
            DataSourceType dataSourceType = DataSourceType.values()[i];
            createSqlSessionFactory(dataSourceType);
        }
    }

    public void createSqlSessionFactory(DataSourceType dataSourceType) {
        try {
            if (dataSourceType.getUrl() == null) {
                logger.log(Level.SEVERE, "DataSource 정보를 찾을 수 없습니다.");
                return;
            }
            createSqlSessionFactory(dataSourceType.toString(), dataSourceType.getEnvironmentId(),
                dataSourceType.getProperties());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public SqlSessionFactory createSqlSessionFactory(String dataSource, String environment, Properties properties)
        throws Exception {
        String environmentId = environment;
        SqlSessionFactory sessionFactory = null;
        try {
            logger.log(Level.INFO, "datasource environment id: " + environmentId);
            sessionFactory = createSqlSessionFactory(environmentId, properties);
            sessionFactoryMap.put(dataSource, sessionFactory);
        } catch (IOException e) {
            Exception exception = new Exception("Data source '" + environmentId + "'를 만드는 도중 오류가 발생하였습니다.", e);
            logger.log(Level.SEVERE, e.getMessage(), exception);
            throw exception;
        }
        return sessionFactory;
    }

    private SqlSessionFactory createSqlSessionFactory(String environment, Properties properties) throws IOException {
        Reader configReader = Resources.getResourceAsReader(CONFIG_RESOURCE);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configReader, environment, properties);
        Configuration configuration = sqlSessionFactory.getConfiguration();

        if (typeAliases != null) {
            for (int i = 0; i < typeAliases.size(); i++) {
                configuration.getTypeAliasRegistry().registerAlias(typeAliases.get(i)[0], typeAliases.get(i)[1]);
            }
        }

        if (mapperResources != null) {
            for (int i = 0; i < mapperResources.size(); i++) {
                String resource = mapperResources.get(i);

                logger.log(Level.INFO, "registerMappers : " + resource);

                ErrorContext.instance().resource(resource);
                InputStream inputStream = Resources.getResourceAsStream(resource);
                XMLMapperBuilder mapperParser =
                    new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                mapperParser.parse();
            }
        }

        return sqlSessionFactory;
    }
}
