package com.kt.ipms.legacy.opermgmt.grantmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;


/** TB_USER_AUTH_TXN DAO INTERFACE **/
@Mapper
public interface TbUserAuthTxnDao {
	
	/** TB_USER_AUTH_TXN insertTbUserAuthTxnVo **/
	public int insertTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);

	/** TB_USER_AUTH_TXN updateTbUserAuthTxnVo **/
	public int updateTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);

	/** TB_USER_AUTH_TXN deleteTbUserAuthTxnVo **/
	public int deleteTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	/** TB_USER_AUTH_TXN deleteTbUserAuthTxnVo **/
	public int deleteTbUserAuthByLvlBasSeq(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	/** TB_USER_AUTH_TXN deleteTbUserAuthTxnVo **/
	public int deleteTbUserAuthListVo(TbUserAuthTxnListVo tbUserAuthTxnListVo);
	
	/** TB_USER_AUTH_TXN deleteTbUserAuthTxnVo **/
	public int deleteTbUserAuthByUserId(TbUserAuthTxnListVo tbUserAuthTxnListVo);

	/** TB_USER_AUTH_TXN selectTbUserAuthTxnVo **/
	public TbUserAuthTxnVo selectTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);

	/** TB_USER_AUTH_TXN selectListTbUserAuthTxnVo **/
	public List<TbUserAuthTxnVo> selectListTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	/** TB_USER_AUTH_TXN countListTbUserAuthTxnVo **/
	public int countListTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	/** TB_USER_AUTH_TXN selectListPageTbUserAuthTxnVo **/
	public List<TbUserAuthTxnVo> selectListPageTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);

	/** TB_USER_AUTH_TXN countListPageTbUserAuthTxnVo **/
	public int countListPageTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo);

	public List<TbLvlBasVo> selectListUserSvcLineList(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	public List<TbLvlBasVo> selectListUserCenterList(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	public List<TbLvlBasVo> selectListUserNodeList(TbUserAuthTxnVo tbUserAuthTxnVo);
	
	public String selectUserGrade(TbUserAuthTxnVo tbUserAuthTxnVo) ;
	
}