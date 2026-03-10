package com.kt.ipms.legacy.ipmgmt.routmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.dao.TbRoutChkMstDao;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;

@Component
public class RoutMgmtTxService {
	
	@Lazy @Autowired
	private TbRoutChkMstDao tbRoutChkMstDao;

	/****************************************************************************************
	 * IP 현행화 관리
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectListPageRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListPageTbRoutChkMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectListExcelRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListExcelRoutChkMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.countListPageTbRoutChkMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageRoutChkMstVo2(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.countListPageTbRoutChkMstVo2(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageRoutChkMstVo3(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.countListPageTbRoutChkMstVo3(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectListTarget(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListTarget(tbRoutChkMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.updateRoutChkMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoutChkExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.updateRoutChkExcptMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbFcltCmdMstVo> selectListFcltOrgCmdMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListFcltOrgCmdMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.insertRoutExcptMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.deleteRoutExcptMstVo(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectListPageRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListPageRoutExcptMstVo(tbRoutChkMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectListIpBlockServiceCheck(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectListIpBlockServiceCheck(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectIpBlockServiceCheckList(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectIpBlockServiceCheckList(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countIpBlockServiceCheckList(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.countIpBlockServiceCheckList(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRoutChkMstVo> selectIpBlockServiceCheckList2(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.selectIpBlockServiceCheckList2(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countIpBlockServiceCheckList2(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.countIpBlockServiceCheckList2(tbRoutChkMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoutMgmtSeq(TbRoutChkMstVo tbRoutChkMstVo) {
		return tbRoutChkMstDao.updateRoutMgmtSeq(tbRoutChkMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectNextHop(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.selectNextHop(ipAllocOperMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countNextHop(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.countNextHop(ipAllocOperMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectLinkMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.selectLinkMst(ipAllocOperMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countLinkMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.countLinkMst(ipAllocOperMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectNextHopHost(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.selectNextHopHost(ipAllocOperMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countNextHopHost(IpAllocOperMstVo ipAllocOperMstVo) {
		return tbRoutChkMstDao.countNextHopHost(ipAllocOperMstVo);
	}
	
}
