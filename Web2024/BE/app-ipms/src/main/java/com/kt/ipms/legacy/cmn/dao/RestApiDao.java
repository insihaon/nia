package com.kt.ipms.legacy.cmn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestApiDao {

	/**
	 * (외부연동 API) IPMS 및 라우팅 IP 정보 수집
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectIpInfo(Map<String,Object> param);
}
