package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;

public interface TbFcltCmdMstDao {

	/**
	 * 장비별 명령어 정보관리 > 목록 조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public List<TbFcltCmdMstVo> selectListPageTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 건수 조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public int countListPageTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 계위별 장비 조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public List<TbFcltCmdMstVo> selectListTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 등록
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public int insertTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 상세 조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public TbFcltCmdMstVo selectTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 수정
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public int updateTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
	
	/**
	 * 장비별 명령어 정보관리 > 삭제
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	public int deleteTbFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo);
}
