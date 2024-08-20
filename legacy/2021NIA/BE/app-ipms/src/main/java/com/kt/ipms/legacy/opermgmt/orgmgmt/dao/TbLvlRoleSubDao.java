package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubVo;


/** TB_LVL_ROLE_SUB DAO INTERFACE **/
public interface TbLvlRoleSubDao {
	
	/** TB_LVL_ROLE_SUB insertTbLvlRoleSubVo **/
	public int insertTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	/** TB_LVL_ROLE_SUB updateTbLvlRoleSubVo **/
	public int updateTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	/** TB_LVL_ROLE_SUB deleteTbLvlRoleSubVo **/
	public int deleteTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	/** TB_LVL_ROLE_SUB selectTbLvlRoleSubVo **/
	public TbLvlRoleSubVo selectTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	/** TB_LVL_ROLE_SUB selectListTbLvlRoleSubVo **/
	public List<TbLvlRoleSubVo> selectListTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);
	
	/** TB_LVL_ROLE_SUB countListTbLvlRoleSubVo **/
	public int countListTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);
	
	/** TB_LVL_ROLE_SUB selectListPageTbLvlRoleSubVo **/
	public List<TbLvlRoleSubVo> selectListPageTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	/** TB_LVL_ROLE_SUB countListPageTbLvlRoleSubVo **/
	public int countListPageTbLvlRoleSubVo(TbLvlRoleSubVo tbLvlRoleSubVo);

	
}