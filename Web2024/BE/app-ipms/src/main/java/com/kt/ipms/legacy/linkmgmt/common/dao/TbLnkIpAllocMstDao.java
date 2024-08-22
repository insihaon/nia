package com.kt.ipms.legacy.linkmgmt.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocMstVo;

@Mapper
public interface TbLnkIpAllocMstDao {
	
	/** TB_IP_ALLOC_MST selectCnt **/
	public int selectCountTbLnkIpAllocMstVo(TbLnkIpAllocMstVo tbLnkIpAllocMstVo);
	
	/** TB_IP_ALLOC_MST select **/
	public List<TbLnkIpAllocMstVo> selectTbLnkIpAllocMstVo(TbLnkIpAllocMstVo tbLnkIpAllocMstVo);

}
