package com.kt.ipms.legacy.opermgmt.limitmgmt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditAssignBasVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;


@Component
@Transactional
public class LimitMgmtService {

	@Autowired
	LimitMgmtTxService limitMgmtTxService;
	
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		TbAuditDhcpBasVo resultVo = null;
		try {
			resultVo = limitMgmtTxService.selectTbAuditDhcpBasVo(tbAuditDhcpBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {	
			throw new ServiceException("CMN.HIGH.00023", new String[]{"임계치기준관리"});
		}	
		 return resultVo;
	}
	
	

	public void updateTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		try {
			limitMgmtTxService.updateTbAuditDhcpBasVo(tbAuditDhcpBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {	
			throw new ServiceException("CMN.HIGH.00020", new String[]{"임계치기준관리"});
		}	
	}
	
	
	public void updateTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo){
		try {
			limitMgmtTxService.updateTbAuditAssignBasVo(tbAuditAssignBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {	
			throw new ServiceException("CMN.HIGH.CMN.HIGH.00020", new String[]{"임계치기준관리"});
		}	
	}	
	
	
	public TbAuditAssignBasVo selectTbAuditAssignBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		TbAuditAssignBasVo resultVo = null;
		try {
			resultVo = limitMgmtTxService.selectTbAuditAssignBasVo(tbAuditDhcpBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {	
			throw new ServiceException("CMN.HIGH.00023", new String[]{"임계치기준관리"});
		}	
		 return resultVo;
	}
	
	
	

}
