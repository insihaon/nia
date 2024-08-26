package com.kt.ipms.legacy.ipmgmt.historymgmt.adapter;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.historymgmt.service.HistoryMgmtService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;


@Component
@Transactional
public class HistoryMgmtAdapterService {

	@Lazy @Autowired
	private HistoryMgmtService historyMgmtService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertHistoryMst(IpHistoryMstVo ipHistoryMstVo) {
		historyMgmtService.insertTbIpHistoryMstVo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<IpHistoryMstVo> selectAllocIpInfoList(IpHistoryMstVo ipHistoryMstVo) {
		return historyMgmtService.selectAllocIpInfoList(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectAllocIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		return historyMgmtService.selectAllocIpInfo(ipHistoryMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectBlockIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		return historyMgmtService.selectBlockIpInfo(ipHistoryMstVo);
	}
}
