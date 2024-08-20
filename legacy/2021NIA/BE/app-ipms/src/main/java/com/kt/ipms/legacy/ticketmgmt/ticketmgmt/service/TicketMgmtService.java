package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;

@Component
@Transactional
public class TicketMgmtService {
	
	@Autowired
	private TicketMgmtTxService ticketMgmtTxService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTicketMst(TbTicketMstVo tbTicketMstVo) {
		ticketMgmtTxService.insertTicketMst(tbTicketMstVo);
	}

}
