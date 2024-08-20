package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstVo;


public interface TbMobileSummMstDao {

	/**
	 * 무선IP 사전 정보관리 > Summary 목록 조회
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public List<TbMobileSummMstVo> selectListPageTbMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > Summary 건수 조회
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public int countListPageTbMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo);

	/**
	 * 무선IP 사전 정보관리 > Summary 중복 조회
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public List<TbMobileSummMstVo> selectListTbMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo);
	
	
	/**
	 * 무선IP 사전 정보관리 > Summary 등록
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public int insertTbMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > Summary 삭제
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public int deleteTbMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > Summary 삭제
	 * @param tbMobileSummMstVo
	 * @return
	 */
	public int deleteTbMobileSummMstAllVo(TbMobileSummMstVo tbMobileSummMstVo);
}
