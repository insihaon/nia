package com.kt.ipms.legacy.ticketmgmt.configmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigLinkMstVo;


/** TB_CONFIG_LINK_MST DAO INTERFACE **/
@Mapper
public interface TbConfigLinkMstDao {
	
	/** TB_CONFIG_LINK_MST insertTbConfigLinkMstVo **/
	public int insertTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	/** TB_CONFIG_LINK_MST updateTbConfigLinkMstVo **/
	public int updateTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	/** TB_CONFIG_LINK_MST deleteTbConfigLinkMstVo **/
	public int deleteTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	/** TB_CONFIG_LINK_MST selectTbConfigLinkMstVo **/
	public TbConfigLinkMstVo selectTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	/** TB_CONFIG_LINK_MST selectListTbConfigLinkMstVo **/
	public List<TbConfigLinkMstVo> selectListTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);
	
	/** TB_CONFIG_LINK_MST countListTbConfigLinkMstVo **/
	public int countListTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);
	
	/** TB_CONFIG_LINK_MST selectListPageTbConfigLinkMstVo **/
	public List<TbConfigLinkMstVo> selectListPageTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	/** TB_CONFIG_LINK_MST countListPageTbConfigLinkMstVo **/
	public int countListPageTbConfigLinkMstVo(TbConfigLinkMstVo tbConfigLinkMstVo);

	
}