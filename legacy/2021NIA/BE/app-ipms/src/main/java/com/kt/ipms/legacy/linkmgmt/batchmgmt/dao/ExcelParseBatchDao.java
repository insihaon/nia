package com.kt.ipms.legacy.linkmgmt.batchmgmt.dao;

import java.util.List;
import java.util.Map;

import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;

public interface ExcelParseBatchDao {

	/**
	 * 수용국 조회
	 * @param map
	 * @return
	 */
	public Map<String,Object> selectLvlNm(Map<String,Object> map);
	
	public int insertIpHostMst(TbIpHostMstVo tbIpHostMstVo);
	
	public int updateIpHostMst(TbIpHostMstVo tbIpHostMstVo);
	
	public int countIpHostMst(TbIpHostMstVo tbIpHostMstVo);
}
