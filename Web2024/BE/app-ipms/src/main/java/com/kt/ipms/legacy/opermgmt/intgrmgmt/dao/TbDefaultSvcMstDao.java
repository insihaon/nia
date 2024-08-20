package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstVo;

public interface TbDefaultSvcMstDao {

	
	/**
	 * 서비스망별 서비스 정보관리 > 목록 조회
	 * @param tbDefaultSvcMstVo
	 * @return
	 */
	public List<TbDefaultSvcMstVo> selectListPageTbDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo);
	
	/**
	 * 서비스망별 서비스 정보관리 > 건수 조회
	 * @param tbDefaultSvcMstVo
	 * @return
	 */
	public int countListPageTbDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo);
	
	/**
	 * 서비스망별 서비스 정보관리 > 등록
	 * @param tbFcltMstVo
	 * @return
	 */
	public int insertTbDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo);
	
	/**
	 * 서비스망별 서비스 정보관리 > 수정
	 * @param tbFcltMstVo
	 * @return
	 */
	public int updateTbDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo);
	
	/**
	 * 서비스망별 서비스 정보관리 > 중복 조회
	 * @param tbDefaultSvcMstVo
	 * @return
	 */
	public List<TbDefaultSvcMstVo> selectListTbDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo);
	
}
