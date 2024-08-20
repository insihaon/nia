package com.kt.ipms.legacy.cmn.intercepter;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;

import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class SQLStatementInterceptor implements Interceptor {

    private final KTLogger logger = KTLoggerFactory.getLogger(SQLStatementInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object returnValue = null;
        InvocationTargetException ex = null;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
            //} catch(Exception ignored) {
        } catch (InvocationTargetException e) {
            ex = e;
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        int size = (returnValue == null) ? 0 : (returnValue instanceof List) ? ((List<?>) returnValue).size() : 1;
        try {
            final Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = null;

            // 매개 변수 가져 오기, 명령문이 설정되면 SQL 문에 매개 변수가 있으며 매개 변수 형식이 맵 형식임을 의미합니다.
            if (args.length > 1) {
                parameter = invocation.getArgs()[1];
            }

            // if (parameter == null
            //         || parameter instanceof Map && ((HashMap<?, ?>) parameter).get("TOTAL_COUNT") == null) {
            // SQL 문의 ID를 가져옵니다.
            if (parameter != null) {
                System.out.println(String.format("parameter=%s", parameter.toString()));
            }
            String sqlId = ms.getId();
            BoundSql boundSql = ms.getBoundSql(parameter);
            Configuration configuration = ms.getConfiguration();
            String sql = printSql(configuration, boundSql, sqlId, size, time);

            // if (parameter != null) {
            //     try {
            //         System.out.println(sql);
            //     } catch (Exception e) {
            //         System.out.println(e.getMessage());
            //     }
            // }

            // }
        } catch (Exception e) {
            logger.error(String.format("SQLStatementInterceptor Exception: %s", e.getMessage()));
        }

        if (ex != null) {
            logger.error(String.format("SQLStatementInterceptor Exception: %s", ex.getTargetException()));
            throw ex;
        }

        return returnValue;
    }

    private String printSql(Configuration configuration, BoundSql boundSql, String sqlId, int size, long time) {
        String sql = showSql(configuration, boundSql);
        System.out.println(String.format("\n--SQL: %s, %s rows, %s ms (%s)", sqlId, size, time,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

        for (String s : sql.split("\n")) {
            System.out.println(s);
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql = boundSql.getSql().replaceAll("\n|\r\n|\n\r", "\n").replaceAll("\n\t\t", "\n\t")
                .replaceAll("\n\\s*\n", "\n") /* 연속된 줄바꿈 제거 */
        ;

        sql = sql.replaceAll("(\n\\s+,)\\s*", "$1	"); /* , 만 있는 라인 수정 */

        Boolean singleLine = false;
        if (singleLine) {
            sql = sql.replaceAll("[\\s]+", " ");
        }

        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?",
                                obj != null ? Matcher.quoteReplacement(getParameterValue(obj)) : "null");
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",
                                obj != null ? Matcher.quoteReplacement(getParameterValue(obj)) : "null");
                    } else {
                        sql = sql.replaceFirst("\\?", "null");
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("setProperties");
        // do nothing
    }

}
