package com.kt.ipms.legacy.ticketmgmt.configmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.configmgmt.service.ConfigMgmtService;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;

@Component
@Transactional
public class ConfigMgmtAdapterService {

	@Autowired
	private ConfigMgmtService configMgmtService;
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstListVo selectListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtService.selectListPageConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countDuplicateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtService.countDuplicateConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtService.insertConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtService.updateConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtService.deleteConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstVo selectConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return configMgmtService.selectConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstListVo selectListMainRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return configMgmtService.selectListMainRouteMst(tbConfigRouteMstVo);
	}
}
