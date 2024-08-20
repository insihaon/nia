package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.dao.TbTicketMstDao;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;

@Component
@Transactional
public class TicketMgmtTxService {

	@Autowired
	private TbTicketMstDao tbTicketMstDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTicketMst(TbTicketMstVo tbTicketMstVo) {
		tbTicketMstDao.insertTbTicketMstVo(tbTicketMstVo);
	}
}
