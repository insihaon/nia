package com.kt.ipms.legacy.opermgmt.assignmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstVo;


/** TB_REQUEST_ASSIGN_MST DAO INTERFACE **/
@Mapper
public interface TbRequestAssignMstDao {
	
	/** TB_REQUEST_ASSIGN_MST insertTbRequestAssignMstVo **/
	public int insertTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	/** TB_REQUEST_ASSIGN_MST updateTbRequestAssignMstVo **/
	public int updateTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	/** TB_REQUEST_ASSIGN_MST deleteTbRequestAssignMstVo **/
	public int deleteTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	/** TB_REQUEST_ASSIGN_MST selectTbRequestAssignMstVo **/
	public TbRequestAssignMstVo selectTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	/** TB_REQUEST_ASSIGN_MST selectListTbRequestAssignMstVo **/
	public List<TbRequestAssignMstVo> selectListTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);
	
	public List<TbRequestAssignMstVo> selectListPreApyAssign(TbRequestAssignMstVo tbRequestAssignMstVo);
	
	/** TB_REQUEST_ASSIGN_MST countListTbRequestAssignMstVo **/
	public int countListTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);
	
	/** TB_REQUEST_ASSIGN_MST selectListPageTbRequestAssignMstVo **/
	public List<TbRequestAssignMstVo> selectListPageTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	/** TB_REQUEST_ASSIGN_MST countListPageTbRequestAssignMstVo **/
	public int countListPageTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo);

	
}