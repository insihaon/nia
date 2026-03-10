package com.kt.ipms.legacy.opermgmt.usermgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserConnHistVo;


/** TB_USER_CONN_HIST DAO INTERFACE **/
@Mapper
public interface TbUserConnHistDao {
	
	/** TB_USER_CONN_HIST insert **/
	public int insertTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;

	/** TB_USER_CONN_HIST update **/
	public int updateTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;

	/** TB_USER_CONN_HIST delete **/
	public int deleteTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;

	/** TB_USER_CONN_HIST select **/
	public TbUserConnHistVo selectTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;

	/** TB_USER_CONN_HIST selectList **/
	public List<TbUserConnHistVo> selectListTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;
	
	/** TB_USER_CONN_HIST selectListPage **/
	public List<TbUserConnHistVo> selectListPageTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;

	/** TB_USER_CONN_HIST countListPage **/
	public int countListPageTbUserConnHistVo(TbUserConnHistVo tbUserConnHistVo) ;
	
	public int insertTbUserConnHist(LoginInfoVo loginInfoVo) ;
	
	public int updateTbUserConnHist(LoginInfoVo loginInfoVo) ;

	public Integer checkIPLogin(LoginInfoVo loginInfoVo);
}