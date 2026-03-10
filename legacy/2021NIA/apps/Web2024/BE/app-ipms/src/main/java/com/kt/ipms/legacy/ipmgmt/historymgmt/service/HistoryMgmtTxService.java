package com.kt.ipms.legacy.ipmgmt.historymgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.historymgmt.dao.TbIpHistoryMstDao;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;

@Component
@Transactional
public class HistoryMgmtTxService {

	@Lazy @Autowired
	private TbIpHistoryMstDao tbIpHistoryMstDao;
	
	
	@Transactional(readOnly = true)
	public List<IpHistoryMstVo> selectListPageIpHistoryMst(IpHistoryMstVo ipHistoryMstVo){
		return tbIpHistoryMstDao.selectListPageIpHistoryMstVo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageIpHistoryMst(IpHistoryMstVo ipHistoryMstVo){
		return tbIpHistoryMstDao.countListPageIpHistoryMstVo(ipHistoryMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void  insertTbIpHistoryMstVo(IpHistoryMstVo ipHistoryMstVo){
		tbIpHistoryMstDao.insertTbIpHistoryMstVo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<IpHistoryMstVo> selectTableYear(IpHistoryMstVo ipHistoryMstVo){
		return tbIpHistoryMstDao.selectTableYear(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<IpHistoryMstVo> selectAllocIpInfoList(IpHistoryMstVo ipHistoryMstVo) {
		return tbIpHistoryMstDao.selectAllocIpInfoList(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectAllocIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		return tbIpHistoryMstDao.selectAllocIpInfo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectBlockIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		return tbIpHistoryMstDao.selectBlockIpInfo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectMainIpInfoMst(IpHistoryMstVo ipHistoryMstVo) {
		return tbIpHistoryMstDao.selectMainIpInfoMst(ipHistoryMstVo);
	}
	
	
	
	
}
