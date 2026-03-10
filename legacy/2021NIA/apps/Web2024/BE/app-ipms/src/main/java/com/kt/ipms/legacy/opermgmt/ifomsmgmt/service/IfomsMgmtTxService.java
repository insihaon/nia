package com.kt.ipms.legacy.opermgmt.ifomsmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.configmgmt.adapter.ConfigMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;

@Component
@Transactional
public class IfomsMgmtTxService {

	@Lazy @Autowired
	private ConfigMgmtAdapterService configMgmtAdapterService;

	@Transactional(readOnly = true)
	public TbConfigInterfaceMstListVo selectListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtAdapterService.selectListPageConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countDuplicateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtAdapterService.countDuplicateConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtAdapterService.insertConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtAdapterService.updateConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtAdapterService.deleteConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstVo selectConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtAdapterService.selectConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
}
