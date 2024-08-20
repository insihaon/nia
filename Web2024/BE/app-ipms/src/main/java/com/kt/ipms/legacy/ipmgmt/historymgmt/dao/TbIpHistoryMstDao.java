package com.kt.ipms.legacy.ipmgmt.historymgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;

public interface TbIpHistoryMstDao {

	
	/** TB_IP_HISTORY selectListPage **/
	public List<IpHistoryMstVo> selectListPageIpHistoryMstVo(IpHistoryMstVo ipHistoryMstVo);
	

	/** TB_IP_HISTORY countListPage **/
	public int countListPageIpHistoryMstVo(IpHistoryMstVo ipHistoryMstVo);
	
	
	/** TB_IP_HISTORY insert **/
	public int insertTbIpHistoryMstVo(IpHistoryMstVo ipHistoryMstVo) ;
	
	/** TB_IP_HISTORY selectTableYear **/
	public List<IpHistoryMstVo> selectTableYear(IpHistoryMstVo ipHistoryMstVo);
	
	/** TB_IP_HISTORY select **/
	public List<IpHistoryMstVo> selectAllocIpInfoList(IpHistoryMstVo ipHistoryMstVo);
	
	/** TB_IP_HISTORY select **/
	public IpHistoryMstVo selectAllocIpInfo(IpHistoryMstVo ipHistoryMstVo);
	
	/** TB_IP_HISTORY select **/
	public IpHistoryMstVo selectBlockIpInfo(IpHistoryMstVo ipHistoryMstVo);
	
	/** TB_IP_HISTORY select **/
	public IpHistoryMstVo selectMainIpInfoMst(IpHistoryMstVo ipHistoryMstVo);
	
}
