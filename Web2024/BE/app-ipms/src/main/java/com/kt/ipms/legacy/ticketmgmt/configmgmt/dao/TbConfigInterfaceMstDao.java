package com.kt.ipms.legacy.ticketmgmt.configmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;


/** TB_CONFIG_INTERFACE_MST DAO INTERFACE **/
public interface TbConfigInterfaceMstDao {
	
	/** TB_CONFIG_INTERFACE_MST insertTbConfigInterfaceMstVo **/
	public int insertTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST updateTbConfigInterfaceMstVo **/
	public int updateTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST deleteTbConfigInterfaceMstVo **/
	public int deleteTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST selectTbConfigInterfaceMstVo **/
	public TbConfigInterfaceMstVo selectTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST selectListTbConfigInterfaceMstVo **/
	public List<TbConfigInterfaceMstVo> selectListTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);
	
	/** TB_CONFIG_INTERFACE_MST countListTbConfigInterfaceMstVo **/
	public int countListTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);
	
	/** TB_CONFIG_INTERFACE_MST selectListTopologyTbConfigInterfaceMstVo **/
	public List<TbConfigInterfaceMstVo> selectListTopologyTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);
	
	/** TB_CONFIG_INTERFACE_MST countListTopologyTbConfigInterfaceMstVo **/
	public int countListTopologyTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);
	
	/** TB_CONFIG_INTERFACE_MST selectListPageTbConfigInterfaceMstVo **/
	public List<TbConfigInterfaceMstVo> selectListPageTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST countListPageTbConfigInterfaceMstVo **/
	public int countListPageTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);

	/** TB_CONFIG_INTERFACE_MST countDuplicateTbConfigInterfaceMstVo **/
	public int countDuplicateTbConfigInterfaceMstVo(TbConfigInterfaceMstVo tbConfigInterfaceMstVo);
}