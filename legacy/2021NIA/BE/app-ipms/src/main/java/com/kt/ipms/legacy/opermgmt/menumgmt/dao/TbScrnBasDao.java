package com.kt.ipms.legacy.opermgmt.menumgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasVo;


/** TB_SCRN_BAS DAO INTERFACE **/
public interface TbScrnBasDao {
	
	/** TB_SCRN_BAS insert **/
	public int insertTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;

	/** TB_SCRN_BAS update **/
	public int updateTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;

	/** TB_SCRN_BAS delete **/
	public int deleteTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;

	/** TB_SCRN_BAS select **/
	public TbScrnBasVo selectTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;

	/** TB_SCRN_BAS selectList **/
	public List<TbScrnBasVo> selectListTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;
	
	/** TB_SCRN_BAS selectListPage **/
	public List<TbScrnBasVo> selectListPageTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;

	/** TB_SCRN_BAS countListPage **/
	public int countListPageTbScrnBasVo(TbScrnBasVo tbScrnBasVo) ;
	
	/** TB_SRCN_BAS updateSscrnUseYn **/
	public int updateSscrnUseYn(TbScrnBasListVo tbScrnBasListVo) ;
	
	/** TB_SRCN_BAS selectNewsscrnId **/
	public String selectNewsscrnId() ;
	
}