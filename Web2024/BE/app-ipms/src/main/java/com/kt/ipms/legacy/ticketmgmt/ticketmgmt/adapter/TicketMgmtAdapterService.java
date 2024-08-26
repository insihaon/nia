package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.service.TicketMgmtService;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;

@Component
@Transactional
public class TicketMgmtAdapterService {

	@Lazy @Autowired
	private TicketMgmtService ticketMgmtService;

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTicketMst(TbTicketMstVo tbTicketMstVo) {
		ticketMgmtService.insertTicketMst(tbTicketMstVo);
	}
}
