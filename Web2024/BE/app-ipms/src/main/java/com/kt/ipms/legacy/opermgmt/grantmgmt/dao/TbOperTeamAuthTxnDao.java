package com.kt.ipms.legacy.opermgmt.grantmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;


/** TB_OPER_TEAM_AUTH_TXN DAO INTERFACE **/
public interface TbOperTeamAuthTxnDao {
	
	/** TB_OPER_TEAM_AUTH_TXN insert **/
	public int insertTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;

	/** TB_OPER_TEAM_AUTH_TXN update **/
	public int updateTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;

	/** TB_OPER_TEAM_AUTH_TXN delete **/
	public int deleteTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;

	/** TB_OPER_TEAM_AUTH_TXN select **/
	public TbOperTeamAuthTxnVo selectTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;

	/** TB_OPER_TEAM_AUTH_TXN selectList **/
	public List<TbOperTeamAuthTxnVo> selectListTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;
	
	/** TB_OPER_TEAM_AUTH_TXN selectListPage **/
	public List<TbOperTeamAuthTxnVo> selectListPageTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;

	/** TB_OPER_TEAM_AUTH_TXN countListPage **/
	public int countListPageTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;
	
	public List<TbLvlBasVo> selectListTeamSvcLineList(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;
	
	public List<TbLvlBasVo> selectListTeamCenterList(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;
	
	public List<TbLvlBasVo> selectListTeamNodeList(TbOperTeamAuthTxnVo tbOperTeamAuthTxnVo) ;
	
}