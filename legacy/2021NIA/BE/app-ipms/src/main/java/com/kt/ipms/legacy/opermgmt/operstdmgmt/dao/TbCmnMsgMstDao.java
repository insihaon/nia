package com.kt.ipms.legacy.opermgmt.operstdmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgMstVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgLvlCdVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgTypeCdVo;


/** TB_CMN_MSG_CD_MST DAO INTERFACE **/
public interface TbCmnMsgMstDao {
	
	/** TB_CMN_MSG_CD_MST insert **/
	public int insertTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;

	/** TB_CMN_MSG_CD_MST update **/
	public int updateTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;

	/** TB_CMN_MSG_CD_MST delete **/
	public int deleteTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;

	/** TB_CMN_MSG_CD_MST select **/
	public TbCmnMsgMstVo selectTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;

	/** TB_CMN_MSG_CD_MST selectList **/
	public List<TbCmnMsgMstVo> selectListTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;
	
	/** TB_CMN_MSG_CD_MST selectListPage **/
	public List<TbCmnMsgMstVo> selectListPageTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;

	/** TB_CMN_MSG_CD_MST countListPage **/
	public int countListPageTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgVo) ;
	
	/** TB_CMN_MSG_LVL_CD selectList **/
	public List<TbCmnMsgLvlCdVo> selectListTbCmnMsgLvlCdVo() ;
	
	/** TB_CMN_MSG_TYPE_CD selectList **/
	public List<TbCmnMsgTypeCdVo> selectListTbCmnMsgTypeCdVo() ;
}