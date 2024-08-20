package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleVo;


/** TB_LVL_ROLE DAO INTERFACE **/
public interface TbLvlRoleDao {
	
	/** TB_LVL_ROLE insert **/
	public int insertTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	/** TB_LVL_ROLE update **/
	public int updateTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	/** TB_LVL_ROLE delete **/
	public int deleteTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	/** TB_LVL_ROLE select **/
	public TbLvlRoleVo selectTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	/** TB_LVL_ROLE selectList **/
	public List<TbLvlRoleVo> selectListTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;
	
	/** TB_LVL_ROLE selectListPage **/
	public List<TbLvlRoleVo> selectListPageTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	/** TB_LVL_ROLE countListPage **/
	public int countListPageTbLvlRoleVo(TbLvlRoleVo tbLvlRoleVo) ;

	
}