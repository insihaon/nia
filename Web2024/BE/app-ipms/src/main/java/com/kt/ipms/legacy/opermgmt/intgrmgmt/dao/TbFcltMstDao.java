package com.kt.ipms.legacy.opermgmt.intgrmgmt.dao;

import java.util.List;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstVo;

public interface TbFcltMstDao {

	/**
	 * 조직별 장비 정보관리 > 목록 조회
	 * @param tbFcltMstVo
	 * @return
	 */
	public List<TbFcltMstVo> selectListPageTbFcltMstVo(TbFcltMstVo tbFcltMstVo);
	
	/**
	 * 조직별 장비 정보관리 > 건수 조회
	 * @param tbFcltMstVo
	 * @return
	 */
	public int countListPageTbFcltMstVo(TbFcltMstVo tbFcltMstVo);

	/**
	 * 조직별 장비 정보관리 > 장비 타입 조회
	 * @param tbFcltMstVo
	 * @return
	 */
	public List<TbFcltMstVo> selectListTbFcltMstVo(TbFcltMstVo tbFcltMstVo);

	/**
	 * 조직별 장비 정보관리 > 등록
	 * @param tbFcltMstVo
	 * @return
	 */
	public int insertTbFcltMstVo(TbFcltMstVo tbFcltMstVo);
	
	/**
	 * 조직별 장비 정보관리 > 상세조회
	 * @param tbFcltMstVo
	 * @return
	 */
	public TbFcltMstVo selectTbFcltMstVo(TbFcltMstVo tbFcltMstVo);
	
	/**
	 * 조직별 장비 정보관리 > 수정
	 * @param tbFcltMstVo
	 * @return
	 */
	public int updateTbFcltMstVo(TbFcltMstVo tbFcltMstVo);
	
	/**
	 * 조직별 장비 정보관리 > 삭제
	 * @param tbFcltMstVo
	 * @return
	 */
	public int deleteTbFcltMstVo(TbFcltMstVo tbFcltMstVo);
}
