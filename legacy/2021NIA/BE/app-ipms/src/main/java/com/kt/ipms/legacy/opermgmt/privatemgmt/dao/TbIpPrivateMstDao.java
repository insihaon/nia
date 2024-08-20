package com.kt.ipms.legacy.opermgmt.privatemgmt.dao;

import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;


public interface TbIpPrivateMstDao {
	
	public List<TbIpPrivateReqMstVo> selectListTbIpPrivateReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public int countListPageIpPrivateReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public TbIpPrivateReqMstVo selectPrivateIPMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public String checkPrivateIpReqValid(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public String selectNipPrivateReqMstSeqCd(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public void insertListIpPrivateReqMstVo(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public int deletePrivateIPMstVo(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public void updatePrivateIPMstAppr(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public TbIpPrivateReqMstVo selectIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo);
	
	public List<TbIpPrivateReqMstVo> selectListTbIpPrivateDelReq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public int countListTbIpPrivateDelReq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public TbIpPrivateReqMstVo selectPrivateDelIPMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public List<TbIpPrivateReqMstVo> selectPrivateIPMstAll(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public List<TbIpPrivateReqMstVo> selectPrivateIPMstSeqCd(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	public List<TbIpPrivateReqMstVo> selectNlvlMstSeq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo);
	
	
}
