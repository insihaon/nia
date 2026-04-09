package com.kt.ipms.legacy.opermgmt.asmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistVo;


/** TB_REQUEST_AS_HIST DAO INTERFACE **/
@Mapper
public interface TbRequestAsHistDao {
	
	/** TB_REQUEST_AS_HIST insertTbRequestAsHistVo **/
	public int insertTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	/** TB_REQUEST_AS_HIST updateTbRequestAsHistVo **/
	public int updateTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	/** TB_REQUEST_AS_HIST deleteTbRequestAsHistVo **/
	public int deleteTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	/** TB_REQUEST_AS_HIST selectTbRequestAsHistVo **/
	public TbRequestAsHistVo selectTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	/** TB_REQUEST_AS_HIST selectListTbRequestAsHistVo **/
	public List<TbRequestAsHistVo> selectListTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);
	
	/** TB_REQUEST_AS_HIST countListTbRequestAsHistVo **/
	public int countListTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);
	
	/** TB_REQUEST_AS_HIST selectListPageTbRequestAsHistVo **/
	public List<TbRequestAsHistVo> selectListPageTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	/** TB_REQUEST_AS_HIST countListPageTbRequestAsHistVo **/
	public int countListPageTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo);

	
}