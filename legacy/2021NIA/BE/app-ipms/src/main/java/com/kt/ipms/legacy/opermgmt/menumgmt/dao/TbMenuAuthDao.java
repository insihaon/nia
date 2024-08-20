package com.kt.ipms.legacy.opermgmt.menumgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthVo;


/** TB_MENU_AUTH DAO INTERFACE **/
public interface TbMenuAuthDao {
	
	/** TB_MENU_AUTH insert **/
	public int insertTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;

	/** TB_MENU_AUTH update **/
	public int updateTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;

	/** TB_MENU_AUTH delete **/
	public int deleteTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;

	/** TB_MENU_AUTH select **/
	public TbMenuAuthVo selectTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;

	/** TB_MENU_AUTH selectList **/
	public List<TbMenuAuthVo> selectListTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;
	
	/** TB_MENU_AUTH selectListPage **/
	public List<TbMenuAuthVo> selectListPageTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;

	/** TB_MENU_AUTH countListPage **/
	public int countListPageTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) ;
	
	/** TB_MENU_AUTH insertMenuAuthUseYn **/
	public void insertMenuAuthUseYn(TbMenuAuthVo tbMenuAuthVo) ;
		
	/** TB_MENU_AUTH DeleteMenuAuthUseYn **/
	public void deleteAuthUseYn(TbMenuAuthVo tbMenuAuthVo) ;
}
