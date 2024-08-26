package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.service.TbAuditDhcpMstService;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;

@Component
@Transactional
public class TbAuditDhcpMstAdapterService {

	@Lazy @Autowired
	private TbAuditDhcpMstService tbAuditDhcpMstService;

	@Transactional(readOnly = true)
	public TbAuditDhcpMstListVo selectListAuditDhcpMst(TbAuditDhcpMstVo searchVo) {
		return tbAuditDhcpMstService.selectListTbAuditDhcpMstVo(searchVo);
	}

}
