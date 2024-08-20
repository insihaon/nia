package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstVo;

public interface TbMobileMstDao {
	
	/**
	 * 무선IP 사전 정보관리 > 목록 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	public List<TbMobileMstVo> selectListPageTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > 건수 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	public int countListPageTbMobileMstVo(TbMobileMstVo tbMobileMstVo);

	/**
	 * 무선IP 사전 정보관리 > 중복 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	public List<TbMobileMstVo> selectListTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	
	/**
	 * 무선IP 사전 정보관리 > 등록
	 * @param tbMobileMstVo
	 * @return
	 */
	public int insertTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > 상세 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	public TbMobileMstVo selectTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > 수정
	 * @param tbMobileMstVo
	 * @return
	 */
	public int updateTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > 삭제
	 * @param tbMobileMstVo
	 * @return
	 */
	public int deleteTbMobileMstVo(TbMobileMstVo tbMobileMstVo);
	
	/**
	 * 무선IP 사전 정보관리 > 삭제
	 * @param tbMobileMstVo
	 * @return
	 */
	public int deleteTbMobileMstAllVo(TbMobileMstVo tbMobileMstVo);
	

}
