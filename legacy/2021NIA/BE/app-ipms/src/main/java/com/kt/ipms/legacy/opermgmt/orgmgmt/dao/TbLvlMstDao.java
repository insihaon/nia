package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;


/** TB_LVL_MST DAO INTERFACE **/
public interface TbLvlMstDao {
	
	/** TB_LVL_MST insert **/
	public int insertTbLvlMstVo(TbLvlRoleMstVo tbLvlBasVo) ;

	/** TB_LVL_MST update **/
	public int updateTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;

	/** TB_LVL_MST delete **/
	public int deleteTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;

	/** TB_LVL_MST select **/
	public TbLvlMstVo selectTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;

	/** TB_LVL_MST selectList **/
	public List<TbLvlMstVo> selectListTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;
	
	/** TB_LVL_MST selectListPage **/
	public List<TbLvlMstVo> selectListPageTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;

	/** TB_LVL_MST countListPage **/
	public int countListPageTbLvlMstVo(TbLvlMstVo tbLvlMstVo) ;
		
	/** TB_LVL_MST selectListMstSeqBySvcLine **/
	public List<TbLvlMstVo> selectListMstSeqBySvcLine(TbLvlMstVo tbLvlMstVo) ;
	
	/** TB_LVL_MST selectListMstSeqByCenter **/
	public List<TbLvlMstVo> selectListMstSeqByCenter(TbLvlMstVo tbLvlMstVo) ;
	
	/** TB_LVL_MST selectListMstSeqByOper **/
	public List<TbLvlMstVo> selectListMstSeqByOper(TbLvlMstVo tbLvlMstVo) ;
	
	
}