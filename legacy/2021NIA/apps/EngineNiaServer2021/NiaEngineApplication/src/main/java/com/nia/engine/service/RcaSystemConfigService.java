package com.nia.engine.service;

import java.util.HashMap;

public interface RcaSystemConfigService {
		/**
	 *  rca 설정값 조회
	 * @param map
	 * @return  String
	 */
	String selectSystemConfig(HashMap<String, String> map) throws Exception;

}
