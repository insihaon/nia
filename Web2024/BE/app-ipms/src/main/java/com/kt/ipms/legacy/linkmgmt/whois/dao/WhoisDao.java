package com.kt.ipms.legacy.linkmgmt.whois.dao;

import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;


/****************************************************
 *  WHOIS 연동 관련  DB Access를 처리하는 Interface Class
 * 
 ****************************************************/
public interface WhoisDao {
	
	public String callUfNewWhois(TbWhoisVo tbWhoisVo);

	public List<TbWhoisComplexVo> selectListWhoisComplexVo(TbWhoisVo tbWhoisVo);
	
	public List<TbWhoisComplexVo> selectListWhoisComplexVo2(TbWhoisVo tbWhoisVo);

	public int updateResultCd(TbWhoisVo tbWhoisVo); 
	
	public int updateTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);
	
	public List<TbWhoisComplexVo> selectListWhoisModifyComplexVo(TbWhoisVo tbWhoisVo);
	
	public int deleteResultCd(TbWhoisVo tbWhoisVo); 
	
	public String deleteResultUserCd(TbWhoisVo tbWhoisVo); 
	
	public TbWhoisComplexVo selectWhoisSeq (TbWhoisVo tbWhoisVo);
	
	public void insertWhoisLog(TbWhoisVo tbWhoisVo);
	
	public TbWhoisVo selectAllocMstSeq (TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public TbWhoisVo selectWhoisSeq (TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public TbIpAllocMstVo selectAllocMst (TbIpAllocMstVo tbIpAllocMstVo);
	
	public int selectNipAllocMstCnt(TbIpAllocMstVo tbIpAllocMstVo);
}
