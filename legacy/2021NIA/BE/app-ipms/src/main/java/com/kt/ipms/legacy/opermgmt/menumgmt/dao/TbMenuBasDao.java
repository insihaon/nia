package com.kt.ipms.legacy.opermgmt.menumgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;


/** TB_MENU_BAS DAO INTERFACE **/
public interface TbMenuBasDao {
	
	/** TB_MENU_BAS insert **/
	public int insertTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	/** TB_MENU_BAS update **/
	public int updateTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	/** TB_MENU_BAS delete **/
	public int deleteTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	/** TB_MENU_BAS select **/
	public TbMenuBasVo selectTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	/** TB_MENU_BAS selectList **/
	public List<TbMenuBasVo> selectListTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;
	
	/** TB_MENU_BAS selectListPage **/
	public List<TbMenuBasVo> selectListPageTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	/** TB_MENU_BAS countListPage **/
	public int countListPageTbMenuBasVo(TbMenuBasVo tbMenuBasVo) ;

	
}