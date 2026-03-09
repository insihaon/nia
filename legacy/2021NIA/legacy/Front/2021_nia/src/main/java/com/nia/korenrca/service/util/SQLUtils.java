package com.nia.korenrca.service.util;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.SqlSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLUtils {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

  public static String Log(SqlSession session, String serviceId, Object properties) {
    String msg = getSqlString(session, serviceId, properties);
    // getLogger().log(Level.FINE, msg);
    return msg;
  }

  public static String Log(SqlSession session, String serviceId, String properties) {
    String msg = getSqlString(session, serviceId, properties);
    // getLogger().log(Level.FINE, msg);
    return msg;
  }

  public static String getSqlString(SqlSession session, String serviceId, Object param) {
    MappedStatement stmt = session.getConfiguration().getMappedStatement(serviceId);
    SqlSource sql = stmt.getSqlSource();
    BoundSql boundSql = null;

    try {
      boundSql = sql.getBoundSql(param);
    } catch (Exception e) {
      getLogger().log(Level.FINE, "\n==> Exception Sql serviceId : " + serviceId + "\n\n" + e.getMessage() + "\n");
    }

    return mapQuery(boundSql, serviceId, param);// .trim();//.replace("\n",
                                                // "");
  }

  public static Object getSqlString(SqlSession session, String serviceId) {
    return getSqlString(session, serviceId, null);
  }

  @SuppressWarnings("unchecked")
  private static String mapQuery(BoundSql boundSql, String serviceId, Object params) {
    // TODO : _parameter 처리로 변경하는거 고려해야함.
    // Object _parameter = boundSql.getAdditionalParameter("_parameter");
    String query = boundSql.getSql();

    if (params != null) {
      for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
        int index = query.indexOf('?');
        if (index == -1) {
          return query;
        }

        ParameterMapping parameterMapping = boundSql.getParameterMappings().get(i);
        String property = parameterMapping.getProperty();

        Object value = boundSql.getAdditionalParameter(property);

        if (value == null) {
          if (params instanceof Map) {
            Map<String, Object> parameter = (Map<String, Object>) params;
            value = parameter.get(property);
          } else {
            value = params;
          }
        }

        query = query.substring(0, index) + getParameter(value) + query.substring(index + 1);
      }
    }

    String[] queryArray = query.split("\n|\r\n|\n\r");
    StringBuilder sb = new StringBuilder();
    for (String str : queryArray) {
      String check = str.replace("\t", "").replace(" ", "");
      if (check.length() > 0) {
        sb.append(str.replace("\t", "") + "\n");
      }
    }

    return "\n[ " + serviceId + " ]\n" + sb.toString();
  }

  private static String getParameter(Object value) {
    if (value instanceof String) {
      String text = (String) value;
      if (text.length() > 1000) {
        return "'" + text.substring(0, 100) + "...'";
      }
      return "'" + value + "'";
    }

    if (value instanceof Number) {
      return value.toString();
    }

    if (value instanceof Date) {
      return "TO_DATE('" + DATE_FORMAT.format((Date) value) + "', 'YYYYMMDDHH24MISS')";
    }

    if (value instanceof byte[]) {
      return "byte[]";
    }

    return null;
  }

  private static Logger getLogger() {
    return Logger.getLogger(SQLUtils.class.getName());
  }
}
