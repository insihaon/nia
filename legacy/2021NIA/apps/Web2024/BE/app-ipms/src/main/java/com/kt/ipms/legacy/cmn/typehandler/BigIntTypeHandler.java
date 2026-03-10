package com.kt.ipms.legacy.cmn.typehandler;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(java.math.BigInteger.class)
public class BigIntTypeHandler extends BaseTypeHandler<Object> {

	@Override
	public BigInteger getNullableResult(ResultSet rs, String arg1)
			throws SQLException {
		return (BigInteger)rs.getObject(arg1);
	}

	@Override
	public BigInteger getNullableResult(ResultSet rs, int arg1)
			throws SQLException {
		return (BigInteger)rs.getObject(arg1);
	}

	@Override
	public BigInteger getNullableResult(CallableStatement cs, int arg1)
			throws SQLException {
		return (BigInteger)cs.getObject(arg1);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Object parameter, JdbcType jdbcType) throws SQLException {
		ps.setObject(i, parameter, jdbcType.TYPE_CODE);
	}

}
