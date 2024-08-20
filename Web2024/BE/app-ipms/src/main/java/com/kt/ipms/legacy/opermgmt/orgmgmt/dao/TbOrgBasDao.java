package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasVo;


/** TB_ORG_BAS DAO INTERFACE **/
public interface TbOrgBasDao {
	
	/** TB_ORG_BAS insert **/
	public int insertTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;

	/** TB_ORG_BAS update **/
	public int updateTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;

	/** TB_ORG_BAS delete **/
	public int deleteTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;

	/** TB_ORG_BAS select **/
	public TbOrgBasVo selectTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;

	/** TB_ORG_BAS selectList **/
	public List<TbOrgBasVo> selectListTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;
	
	/** TB_ORG_BAS selectListPage **/
	public List<TbOrgBasVo> selectListPageTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;

	/** TB_ORG_BAS countListPage **/
	public int countListPageTbOrgBasVo(TbOrgBasVo tbOrgBasVo) ;
	
	/** TB_ORG_BAS updateSipmsOrgYn **/
	public int updateSipmsOrgYn(TbOrgBasListVo tbOrgBasListVo) ;

	
}