/*
 *  version 1.0
 *
 *  Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 */

package com.codej.base.dto.model;

import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("rawtypes")
public class DataMap extends org.apache.commons.collections4.map.ListOrderedMap {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
	}

	public Object get(String key, Object retunNullValue) {
		Object obj = super.get(key);
		if (obj != null) {
			return obj;
		} else {
			return retunNullValue;
		}
	}
}