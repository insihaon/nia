package com.nia.engine.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface RcaSystemConfigMapper {
	/**
	 * 티켓 key 생성
	 * @param
	 * @return 
	 */
    String selectSystemConfig(HashMap<String, String> map);

	
}
