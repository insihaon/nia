package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;

/** TB_WHOIS_COMPLEX DAO INTERFACE **/
@Mapper
public interface TbWhoisComplexDao {
	
	/** TB_WHOIS_COMPLEX selectListWhoisComplexVo **/
	public List<TbWhoisComplexVo> selectListTbWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo);
	
	/** TB_WHOIS_COMPLEX countListWhoisComplexVo **/
	public int countListTbWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo);
	
	/** TB_WHOIS_COMPLEX selectListBatchWhoisComplexVo **/
	public List<TbWhoisComplexVo> selectListBatchWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo);
	
	/** TB_WHOIS_COMPLEX countListBatchWhoisComplexVo **/
	public int countListBatchWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo);
}
