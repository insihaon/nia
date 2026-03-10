package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdVo;


/** TB_ASSIGN_TYPE_CD DAO INTERFACE **/
@Mapper
public interface TbAssignTypeCdDao {
	
	/** TB_ASSIGN_TYPE_CD insertTbAssignTypeCdVo **/
	public int insertTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	/** TB_ASSIGN_TYPE_CD updateTbAssignTypeCdVo **/
	public int updateTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	/** TB_ASSIGN_TYPE_CD deleteTbAssignTypeCdVo **/
	public int deleteTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	/** TB_ASSIGN_TYPE_CD selectTbAssignTypeCdVo **/
	public TbAssignTypeCdVo selectTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	/** TB_ASSIGN_TYPE_CD selectListTbAssignTypeCdVo **/
	public List<TbAssignTypeCdVo> selectListTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);
	
	/** TB_ASSIGN_TYPE_CD countListTbAssignTypeCdVo **/
	public int countListTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);
	
	/** TB_ASSIGN_TYPE_CD selectListPageTbAssignTypeCdVo **/
	public List<TbAssignTypeCdVo> selectListPageTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	/** TB_ASSIGN_TYPE_CD countListPageTbAssignTypeCdVo **/
	public int countListPageTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo);

	
}