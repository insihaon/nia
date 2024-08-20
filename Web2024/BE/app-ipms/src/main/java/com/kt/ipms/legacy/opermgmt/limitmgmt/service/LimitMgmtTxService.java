package com.kt.ipms.legacy.opermgmt.limitmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.limitmgmt.dao.TbAuditAssignBasDao;
import com.kt.ipms.legacy.opermgmt.limitmgmt.dao.TbAuditDhcpBasDao;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditAssignBasVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;

@Component
@Transactional
public class LimitMgmtTxService {
	
	@Autowired
	private TbAuditDhcpBasDao tbAuditDhcpBasDao;

	@Autowired
	private TbAuditAssignBasDao tbAuditAssignBasDao;
	

	
	@Transactional(readOnly = true)
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		return tbAuditDhcpBasDao.selectTbAuditDhcpBasVo(tbAuditDhcpBasVo);
	}
	

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		return tbAuditDhcpBasDao.updateTbAuditDhcpBasVo(tbAuditDhcpBasVo);
	}
	
	

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo){
		return tbAuditAssignBasDao.updateTbAuditAssignBasVo(tbAuditAssignBasVo);
	}
	

	
	@Transactional(readOnly = true)
	public TbAuditAssignBasVo selectTbAuditAssignBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		return tbAuditAssignBasDao.selectTbAuditAssignBasVo(tbAuditDhcpBasVo);
	}
	
	
	
}
