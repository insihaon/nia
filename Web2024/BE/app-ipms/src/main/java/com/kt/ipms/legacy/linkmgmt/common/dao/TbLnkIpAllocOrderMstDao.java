package com.kt.ipms.legacy.linkmgmt.common.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocOrderMstVo;

/** TB_LNK_IP_ALLOC_ORDER_DAO INTERFACE **/
@Mapper
public interface TbLnkIpAllocOrderMstDao {
	
	/** TB_IP_ALLOC_ORDER_MST insert **/
	public int insertTbLnkIpAllocOrderMstVo(TbLnkIpAllocOrderMstVo tbLnkIpAllocOrderMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST update **/
	public int updateTbLnkIpAllocOrderMstVo(TbLnkIpAllocOrderMstVo tbLnkIpAllocOrderMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST delete **/
	public int deleteTbLnkIpAllocOrderMstVo(TbLnkIpAllocOrderMstVo tbLnkIpAllocOrderMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST select **/
	public TbLnkIpAllocOrderMstVo selectTbLnkIpAllocOrderMstVo(TbLnkIpAllocOrderMstVo tbLnkIpAllocOrderMstVo);
}
