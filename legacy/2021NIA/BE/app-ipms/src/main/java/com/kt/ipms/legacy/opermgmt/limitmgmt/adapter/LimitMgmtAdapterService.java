package com.kt.ipms.legacy.opermgmt.limitmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.limitmgmt.service.LimitMgmtService;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;

@Component
@Transactional
public class LimitMgmtAdapterService {

	@Autowired
	private LimitMgmtService limitMgmtService;
	
	@Transactional(readOnly = true)
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo){
		return limitMgmtService.selectTbAuditDhcpBasVo(tbAuditDhcpBasVo);
	}
	
	
	
}
