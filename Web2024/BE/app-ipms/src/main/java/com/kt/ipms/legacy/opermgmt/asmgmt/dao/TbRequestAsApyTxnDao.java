package com.kt.ipms.legacy.opermgmt.asmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnVo;

/** TB_REQUEST_AS_APY_TXN DAO INTERFACE **/
@Mapper
public interface TbRequestAsApyTxnDao {
	
	/** TB_REQUEST_AS_APY_TXN insert **/
	public int insertTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN update **/
	public int updateTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN delete **/
	public int deleteTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN select **/
	public TbRequestAsApyTxnVo selectTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN selectList **/
	public List<TbRequestAsApyTxnVo> selectListTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN selectListPage **/
	public List<TbRequestAsApyTxnVo> selectListPageTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN countListPageTbRequestAsMstVo **/
	public int countListTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) ;
	
	/** TB_REQUEST_AS_APY_TXN updateNrequestAsSeqYn **/
	public int updateNrequestAsSeqYn(TbRequestAsApyTxnVo tbRequestAsApyTxnVo);
	
	/**	TB_REQUEST_AS_APY_TXN insertAsHistVo **/
	public int insertAsHistVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo);

}
