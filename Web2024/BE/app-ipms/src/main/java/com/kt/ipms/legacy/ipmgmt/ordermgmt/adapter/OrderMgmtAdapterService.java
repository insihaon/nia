package com.kt.ipms.legacy.ipmgmt.ordermgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.ordermgmt.service.OrderMgmtService;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstListVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;

@Component
@Transactional
public class OrderMgmtAdapterService {

	@Lazy @Autowired
	private OrderMgmtService orderMgmtService;

	@Transactional(readOnly = true)
	public IpAllocOrderMstListVo selectListMainOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo) {
		return orderMgmtService.selectListIpAllocOrderMst(ipAllocOrderMstVo);
	}

}
