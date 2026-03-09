package com.kt.ipms.legacy.opermgmt.tacsmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.tacsmgmt.service.TacsMgmtService;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;

@Component
public class TacsMgmtAdapterService {
	
@Lazy @Autowired
	private TacsMgmtService tacsMgmtService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertTacsConnHistNew(TbTacsConnHistVo tbTacsConnHistVo) {
		tacsMgmtService.insertTacsConnHist(tbTacsConnHistVo);
	}

}
