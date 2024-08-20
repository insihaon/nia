package com.kt.ipms.legacy.opermgmt.menumgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnAuthVo;


/** TB_SCRN_AUTH DAO INTERFACE **/
public interface TbScrnAuthDao {
	
	/** TB_SCRN_AUTH insert **/
	public int insertTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	/** TB_SCRN_AUTH update **/
	public int updateTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	/** TB_SCRN_AUTH delete **/
	public int deleteTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	/** TB_SCRN_AUTH select **/
	public TbScrnAuthVo selectTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	/** TB_SCRN_AUTH selectList **/
	public List<TbScrnAuthVo> selectListTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;
	
	/** TB_SCRN_AUTH selectListPage **/
	public List<TbScrnAuthVo> selectListPageTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	/** TB_SCRN_AUTH countListPage **/
	public int countListPageTbScrnAuthVo(TbScrnAuthVo tbScrnAuthVo) ;

	
}