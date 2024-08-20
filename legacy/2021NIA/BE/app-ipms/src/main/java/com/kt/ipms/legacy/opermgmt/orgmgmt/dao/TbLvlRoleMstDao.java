package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;


/** TB_LVL_ROLE_MST DAO INTERFACE **/
public interface TbLvlRoleMstDao {
	
	/** TB_LVL_ROLE_MST insertTbLvlRoleMstVo **/
	public int insertTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	/** TB_LVL_ROLE_MST updateTbLvlRoleMstVo **/
	public int updateTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	/** TB_LVL_ROLE_MST deleteTbLvlRoleMstVo **/
	public int deleteTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	/** TB_LVL_ROLE_MST selectTbLvlRoleMstVo **/
	public TbLvlRoleMstVo selectTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	/** TB_LVL_ROLE_MST selectListTbLvlRoleMstVo **/
	public List<TbLvlRoleMstVo> selectListTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);
	
	/** TB_LVL_ROLE_MST countListTbLvlRoleMstVo **/
	public int countListTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);
	
	/** TB_LVL_ROLE_MST selectListPageTbLvlRoleMstVo **/
	public List<TbLvlRoleMstVo> selectListPageTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	/** TB_LVL_ROLE_MST countListPageTbLvlRoleMstVo **/
	public int countListPageTbLvlRoleMstVo(TbLvlRoleMstVo tbLvlRoleMstVo);

	
}