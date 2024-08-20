package com.kt.ipms.legacy.cmn.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.ipms.legacy.cmn.dao.RestApiDao;


@Component
public class RestApiService {

	@Autowired
	private RestApiDao restApiDao;
	
	/**
	 * (외부연동 API) IPMS 및 라우팅 IP 정보 수집
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectIpInfo(Map<String,Object> param) {
		return restApiDao.selectIpInfo(param);
		
	}
}
