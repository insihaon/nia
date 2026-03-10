package com.kt.ipms.legacy.opermgmt.grantmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;


/** TB_ADMR_APV_TXN DAO INTERFACE **/
@Mapper
public interface TbAdmrApvTxnDao {
	
	/** TB_ADMR_APV_TXN insert **/
	public int insertTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	/** TB_ADMR_APV_TXN update **/
	public int updateTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	/** TB_ADMR_APV_TXN delete **/
	public int deleteTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	/** TB_ADMR_APV_TXN select **/
	public TbAdmrApvTxnVo selectTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	/** TB_ADMR_APV_TXN selectList **/
	public List<TbAdmrApvTxnVo> selectListTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;
	
	/** TB_ADMR_APV_TXN selectListPage **/
	public List<TbAdmrApvTxnVo> selectListPageTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	/** TB_ADMR_APV_TXN countListPage **/
	public int countListPageTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;
	
	public String selectUserGradeAdmr(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;
	
	public List<TbLvlBasVo> selectListSvcLine(TbAdmrApvTxnVo tbAdmrApvTxnVo) ;

	
}