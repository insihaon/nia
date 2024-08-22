package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdVo;


/** TB_LVL_SUB_CD DAO INTERFACE **/
@Mapper
public interface TbLvlSubCdDao {
	
	/** TB_LVL_SUB_CD insertTbLvlSubCdVo **/
	public int insertTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);

	/** TB_LVL_SUB_CD updateTbLvlSubCdVo **/
	public int updateTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);

	/** TB_LVL_SUB_CD deleteTbLvlSubCdVo **/
	public int deleteTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);

	/** TB_LVL_SUB_CD selectTbLvlSubCdVo **/
	public TbLvlSubCdVo selectTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);

	/** TB_LVL_SUB_CD selectListTbLvlSubCdVo **/
	public List<TbLvlSubCdVo> selectListTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);
	
	/** TB_LVL_SUB_CD countListTbLvlSubCdVo **/
	public int countListTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);
	
	/** TB_LVL_SUB_CD selectListPageTbLvlSubCdVo **/
	public List<TbLvlSubCdVo> selectListPageTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);

	/** TB_LVL_SUB_CD countListPageTbLvlSubCdVo **/
	public int countListPageTbLvlSubCdVo(TbLvlSubCdVo tbLvlSubCdVo);
		
	public int selectSloffice(TbLvlSubCdVo tbLvlSubCdVo);

	
}