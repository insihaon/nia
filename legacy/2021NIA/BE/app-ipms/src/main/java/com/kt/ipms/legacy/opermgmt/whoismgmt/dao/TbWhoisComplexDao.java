package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;

/** TB_WHOIS_COMPLEX DAO INTERFACE **/
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
