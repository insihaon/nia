package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.dao.ExcelParseBatchDao;

@Component
public class ExcelParseBatchTxService {

	@Lazy @Autowired
	private ExcelParseBatchDao excelParseBatchDao;
	
	/**
	 * 수용국 조회
	 * @param map
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String,Object> selectLvlNm(Map<String,Object> map) {
		return excelParseBatchDao.selectLvlNm(map);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return excelParseBatchDao.insertIpHostMst(tbIpHostMstVo);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return excelParseBatchDao.updateIpHostMst(tbIpHostMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		return excelParseBatchDao.countIpHostMst(tbIpHostMstVo);
	}
}
