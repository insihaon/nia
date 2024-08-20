package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstVo;

public interface TbWireMstDao {

	/**
	 * 유선 커뮤니티 관리 > 목록 조회
	 * @param tbWireMstVo
	 * @return
	 */
	List<TbWireMstVo> selectListPageWireMstVo(TbWireMstVo tbWireMstVo);

	/**
	 * 유선 커뮤니티 관리 > 건수 조회
	 * @param tbWireMstVo
	 * @return
	 */
	int countListPageWireMstVo(TbWireMstVo tbWireMstVo);
	
	/**
	 * 유선 커뮤니티 관리 > 상세 ㅈ회
	 * @param tbWireMstVo
	 * @return
	 */
	List<TbWireMstVo> selectWireMst(TbWireMstVo tbWireMstVo);
	
	/**
	 * 유선 커뮤니티 관리 > 등록
	 * @param tbWireMstVo
	 */
	void insertWireMst(TbWireMstVo tbWireMstVo);
	
	/**
	 * 유선 커뮤니티 관리 > 수정
	 * @param tbWireMstVo
	 */
	void updateWireMst(TbWireMstVo tbWireMstVo);
	
	/**
	 * 유선 커뮤니티 관리 > 삭제
	 * @param tbWireMstVo
	 */
	void deleteWireMst(TbWireMstVo tbWireMstVo);

	/**
	 * 유선 커뮤니티 관리 > 엑셀
	 * @param tbWireMstVo
	 */
	List<TbWireMstVo> selectListWireMstExcel(TbWireMstVo tbWireMstVo);



}
