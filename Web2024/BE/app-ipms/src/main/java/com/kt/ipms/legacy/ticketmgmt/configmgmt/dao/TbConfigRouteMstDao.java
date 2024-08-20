package com.kt.ipms.legacy.ticketmgmt.configmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;


/** TB_CONFIG_ROUTE_MST DAO INTERFACE **/
public interface TbConfigRouteMstDao {
	
	/** TB_CONFIG_ROUTE_MST insertTbConfigRouteMstVo **/
	public int insertTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST updateTbConfigRouteMstVo **/
	public int updateTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST deleteTbConfigRouteMstVo **/
	public int deleteTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST selectTbConfigRouteMstVo **/
	public TbConfigRouteMstVo selectTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST selectListTbConfigRouteMstVo **/
	public List<TbConfigRouteMstVo> selectListTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);
	
	/** TB_CONFIG_ROUTE_MST countListTbConfigRouteMstVo **/
	public int countListTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);
	
	/** TB_CONFIG_ROUTE_MST selectListPageTbConfigRouteMstVo **/
	public List<TbConfigRouteMstVo> selectListPageTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST countListPageTbConfigRouteMstVo **/
	public int countListPageTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);
	
	/** TB_CONFIG_ROUTE_MST selectListPageMainTbConfigRouteMstVo **/
	public List<TbConfigRouteMstVo> selectListPageMainTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);

	/** TB_CONFIG_ROUTE_MST countListPageMainTbConfigRouteMstVo **/
	public int countListPageMainTbConfigRouteMstVo(TbConfigRouteMstVo tbConfigRouteMstVo);
	
}