package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.adapter.LimitMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.dao.TbAuditDhcpMstDao;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;
@Component
@Transactional
public class TbAuditDhcpMstTxService {

	@Lazy @Autowired
	private TbAuditDhcpMstDao tbAuditDhcpMstDao;
	
	@Lazy @Autowired
	private LimitMgmtAdapterService limitMgmtAdapterService;
	
	@Transactional(readOnly = true)
	public List<TbAuditDhcpMstVo> selectListTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)  {

		List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstDao.selectListTbAuditDhcpMstVo(searchVo);
		
		return resultList;
	}
	
	
	@Transactional(readOnly = true)
	public List<TbAuditDhcpMstVo> selectListPageTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)  {

		List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstDao.selectListPageTbAuditDhcpMstVo(searchVo);
		
		return resultList;
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)  {
		
		int totalCount = tbAuditDhcpMstDao.countListPageTbAuditDhcpMstVo(searchVo);
		
		return totalCount;
	}
	
	
	@Transactional(readOnly = true)
	public TbAuditDhcpMstVo selectTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)  {

		TbAuditDhcpMstVo result = tbAuditDhcpMstDao.selectTbAuditDhcpMstVo(searchVo);
		
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo){
		
	    tbAuditDhcpMstDao.updateTbAuditDhcpMstVo(searchVo);

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbAuditDhcpMstBflagY(TbAuditDhcpMstVo searchVo){
		
		 tbAuditDhcpMstDao.updateTbAuditDhcpMstBflagY(searchVo);
		
	}
	
	@Transactional(readOnly = true)
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo searchVo) {
		return limitMgmtAdapterService.selectTbAuditDhcpBasVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrderSofficeList(TbAuditDhcpMstVo searchVo){
		return tbAuditDhcpMstDao.selectOrderSofficeList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbAuditDhcpMstVo> selectSetOrderOfficeList(TbAuditDhcpMstVo searchVo){
		return tbAuditDhcpMstDao.selectSetOrderOfficeList(searchVo);
	}
	
	
}
