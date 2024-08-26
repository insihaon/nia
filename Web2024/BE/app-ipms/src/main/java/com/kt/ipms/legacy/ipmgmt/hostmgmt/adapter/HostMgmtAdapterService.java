package com.kt.ipms.legacy.ipmgmt.hostmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.hostmgmt.service.HostMgmtService;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;

@Component
public class HostMgmtAdapterService {
	
	@Autowired
	HostMgmtService hostMgmtService;
	
	@Transactional(readOnly = true)	
	public TbIpHostMstListVo selectTbIpHostInfoVo(TbIpHostMstVo tbIpHostMstVo){

		return hostMgmtService.selectTbIpHostInfoVo(tbIpHostMstVo);
	}
}
