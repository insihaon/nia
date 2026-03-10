package com.kt.ipms.legacy.opermgmt.menumgmt.adapter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.menumgmt.service.MenuMgmtService;

import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;


@Component
@Transactional
public class MenuMgmtAdapterService {
	
	@Autowired
	MenuMgmtService menuMgmtService;
	
	@Transactional(readOnly = true)
	public TbMenuBasListVo selectListTbMenuBas(TbMenuBasVo tbMenuBasVo)  {
		return menuMgmtService.selectListTbMenuBas(tbMenuBasVo);
	}
	

}
