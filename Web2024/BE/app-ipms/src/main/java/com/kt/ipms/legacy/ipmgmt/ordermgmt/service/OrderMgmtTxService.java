package com.kt.ipms.legacy.ipmgmt.ordermgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.dao.IpAllocOrderMstDao;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;

@Component
@Transactional
public class OrderMgmtTxService {
	
	@Autowired
	private IpAllocOrderMstDao ipAllocOrderMstDao;
	
	/*오더 목록 Select*/
	@Transactional(readOnly = true)
	public List<IpAllocOrderMstVo> selectListIpAllocOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo){
		return ipAllocOrderMstDao.selectListPageTbIpAllocOrderMstVo(ipAllocOrderMstVo);
	}
	
	/*오더 목록 Select Total count*/
	@Transactional(readOnly = true)
	public int countListPageIpAllocOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo){
		return ipAllocOrderMstDao.countListPageTbIpAllocOrderMstVo(ipAllocOrderMstVo);
	}
	
	/*상품정보 셋팅*/
	@Transactional(readOnly = true)
	public IpAllocOrderMstVo selectNipmsSvcSeqMst(IpAllocOperMstVo ipAllocOperMstVo){
		return ipAllocOrderMstDao.selectNipmsSvcSeqMst(ipAllocOperMstVo);
	}
	
	/*오더 종료(하당) Update*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateFinIpAllocOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo){
		ipAllocOrderMstDao.updateFinIpAllocOrderMst(ipAllocOrderMstVo);
	}
	
	/*서비스 정보 셋팅*/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListSassignLevelCds(IpAllocOperMstVo ipAllocOperMstVo){
		return ipAllocOrderMstDao.selectListSassignLevelCds(ipAllocOperMstVo);
	}
	
	/* 수용국 정보 조회(최초) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrderSofficeList(IpAllocOrderMstVo ipAllocOrderMstVo){
		return ipAllocOrderMstDao.selectOrderSofficeList(ipAllocOrderMstVo);
	}
	
	/* 수용국 정보 조회 */
	@Transactional(readOnly = true)
	public List<IpAllocOrderMstVo> selectSetOrderOfficeList(IpAllocOrderMstVo ipAllocOrderMstVo){
		return ipAllocOrderMstDao.selectSetOrderOfficeList(ipAllocOrderMstVo);
	}
	
}
