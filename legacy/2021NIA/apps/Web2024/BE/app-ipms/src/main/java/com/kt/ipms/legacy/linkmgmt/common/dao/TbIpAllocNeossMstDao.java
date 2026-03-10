package com.kt.ipms.legacy.linkmgmt.common.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;


/** TB_IP_ALLOC_NEOSS_MST DAO INTERFACE **/
@Mapper
public interface TbIpAllocNeossMstDao {
	
	/** TB_IP_ALLOC_NEOSS_MST insert **/
	public int insertTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);

	/** TB_IP_ALLOC_NEOSS_MST update **/
	public int updateTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);

	/** TB_IP_ALLOC_NEOSS_MST delete **/
	public int deleteTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);

	/** TB_IP_ALLOC_NEOSS_MST select **/
	public TbIpAllocNeossMstVo selectTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);

	/** TB_IP_ALLOC_NEOSS_MST selectList **/
	public List<TbIpAllocNeossMstVo> selectListTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/** TB_IP_ALLOC_NEOSS_MST countListPage **/
	public int countListPageTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/** TB_IP_ALLOC_NEOSS_MST countOpenOdr **/
	public int countOpenOdrTbIpAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/** TB_IP_ALLOC_NEOSS_MST selectDefaultOdrInfo**/
	public TbIpAllocNeossMstVo selectDefaultOdrAllocNeossMstVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
}