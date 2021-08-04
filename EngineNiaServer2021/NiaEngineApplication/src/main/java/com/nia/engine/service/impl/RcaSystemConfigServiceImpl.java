package com.nia.engine.service.impl;

import com.nia.engine.mapper.RcaSystemConfigMapper;
import com.nia.engine.service.RcaSystemConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("RcaSystemConfigService")
public class RcaSystemConfigServiceImpl implements RcaSystemConfigService {
	private final Logger LOGGER = Logger.getLogger(RcaSystemConfigServiceImpl.class);

	@Autowired
	private RcaSystemConfigMapper rcaSystemConfigMapper;

	/**
	 *  rca 설정값 조회
	 * @param map
	 * @return  String
	 */
	@Override
	public String selectSystemConfig(HashMap<String, String> map) throws Exception {
		return rcaSystemConfigMapper.selectSystemConfig(map);
	}

}
