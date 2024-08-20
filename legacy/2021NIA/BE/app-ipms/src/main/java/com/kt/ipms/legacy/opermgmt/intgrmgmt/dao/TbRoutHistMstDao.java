package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;

public interface TbRoutHistMstDao {


	/**
	 * 라우팅 연동 이력관리 > 목록 조회
	 * @param tbDefaultSvcMstVo
	 * @return
	 */
	public List<TbRoutHistMstVo> selectListPageTbRoutHistVo(TbRoutHistMstVo tbRoutHistMstVo);
	
	/**
	 * 라우팅 연동 이력관리 > 건수 조회
	 * @param tbDefaultSvcMstVo
	 * @return
	 */
	public int countListTbRoutHistVo(TbRoutHistMstVo tbRoutHistMstVo);
	
	/**
	 * 라우팅 연동 이력관리 > 수정
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int updateRoutHistMstVo(TbRoutHistMstVo tbRoutHistMstVo);
	
	public List<?> selectSresultMsg();
}
