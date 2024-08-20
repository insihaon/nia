package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;


/** TB_LVL_CD DAO INTERFACE **/
public interface TbLvlCdDao {
	
	/** TB_LVL_CD insert **/
	public int insertTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;

	/** TB_LVL_CD update **/
	public int updateTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;

	/** TB_LVL_CD delete **/
	public int deleteTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;

	/** TB_LVL_CD select **/
	public TbLvlCdVo selectTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;

	/** TB_LVL_CD selectList **/
	public List<TbLvlCdVo> selectListTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;
	
	/** TB_LVL_CD selectListPage **/
	public List<TbLvlCdVo> selectListPageTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;

	/** TB_LVL_CD countListPage **/
	public int countListPageTbLvlCdVo(TbLvlCdVo tbLvlCdVo) ;
	
	/** TB_LVL_CD selectNewLvlCd **/
	public String selectNewLvlCd() ;

	
}