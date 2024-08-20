package com.kt.ipms.legacy.ticketmgmt.optmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;


/** TB_OPTIMIZATION_IP_MST DAO INTERFACE **/
public interface TbOptimizationIpMstDao {
	
	/** TB_OPTIMIZATION_IP_MST insertTbOptimizationIpMstVo **/
	public int insertTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	/** TB_OPTIMIZATION_IP_MST updateTbOptimizationIpMstVo **/
	public int updateTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	/** TB_OPTIMIZATION_IP_MST deleteTbOptimizationIpMstVo **/
	public int deleteTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	/** TB_OPTIMIZATION_IP_MST selectTbOptimizationIpMstVo **/
	public TbOptimizationIpMstVo selectTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	/** TB_OPTIMIZATION_IP_MST selectListTbOptimizationIpMstVo **/
	public List<TbOptimizationIpMstVo> selectListTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);
	
	/** TB_OPTIMIZATION_IP_MST countListTbOptimizationIpMstVo **/
	public int countListTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);
	
	/** TB_OPTIMIZATION_IP_MST selectListPageTbOptimizationIpMstVo **/
	public List<TbOptimizationIpMstVo> selectListPageTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	/** TB_OPTIMIZATION_IP_MST countListPageTbOptimizationIpMstVo **/
	public int countListPageTbOptimizationIpMstVo(TbOptimizationIpMstVo tbOptimizationIpMstVo);

	
}