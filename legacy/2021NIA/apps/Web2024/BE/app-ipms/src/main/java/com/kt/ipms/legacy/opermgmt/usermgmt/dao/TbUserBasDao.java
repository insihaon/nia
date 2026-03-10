package com.kt.ipms.legacy.opermgmt.usermgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;


/** TB_USER_BAS DAO INTERFACE **/
@Mapper
public interface TbUserBasDao {
	
	/** TB_USER_BAS insert **/
	public int insertTbUserBasVo(TbUserBasVo tbUserBasVo) ;

	/** TB_USER_BAS update **/
	public int updateTbUserBasVo(TbUserBasVo tbUserBasVo) ;

	/** TB_USER_BAS delete **/
	public int deleteTbUserBasVo(TbUserBasVo tbUserBasVo) ;

	/** TB_USER_BAS select **/
	public TbUserBasVo selectTbUserBasVo(TbUserBasVo tbUserBasVo) ;

	/** TB_USER_BAS selectList **/
	public List<TbUserBasVo> selectListTbUserBasVo(TbUserBasVo tbUserBasVo) ;
	
	/** TB_USER_BAS selectListPage **/
	public List<TbUserBasVo> selectListPageTbUserBasVo(TbUserBasVo tbUserBasVo) ;

	/** TB_USER_BAS countListPage **/
	public int countListPageTbUserBasVo(TbUserBasVo tbUserBasVo) ;
	
	/** TB_USER_BAS select **/
	public String selectEmail(TbUserBasVo tbUserBasVo) ;

	
}