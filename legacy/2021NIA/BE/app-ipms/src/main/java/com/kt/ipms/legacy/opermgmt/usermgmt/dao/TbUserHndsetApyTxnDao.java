package com.kt.ipms.legacy.opermgmt.usermgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserHndsetApyTxnVo;


/** TB_USER_HNDSET_APY_TXN DAO INTERFACE **/
public interface TbUserHndsetApyTxnDao {
	
	/** TB_USER_HNDSET_APY_TXN insert **/
	public int insertTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	/** TB_USER_HNDSET_APY_TXN update **/
	public int updateTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	/** TB_USER_HNDSET_APY_TXN delete **/
	public int deleteTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	/** TB_USER_HNDSET_APY_TXN select **/
	public TbUserHndsetApyTxnVo selectTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	/** TB_USER_HNDSET_APY_TXN selectList **/
	public List<TbUserHndsetApyTxnVo> selectListTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;
	
	/** TB_USER_HNDSET_APY_TXN selectListPage **/
	public List<TbUserHndsetApyTxnVo> selectListPageTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	/** TB_USER_HNDSET_APY_TXN countListPage **/
	public int countListPageTbUserHndsetApyTxnVo(TbUserHndsetApyTxnVo tbUserHndsetApyTxnVo) ;

	
}