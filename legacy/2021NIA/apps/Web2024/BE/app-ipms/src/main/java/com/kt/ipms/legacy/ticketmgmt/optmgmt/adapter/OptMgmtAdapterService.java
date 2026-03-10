package com.kt.ipms.legacy.ticketmgmt.optmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ticketmgmt.optmgmt.service.OptMgmtService;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.optmgmt.vo.TbOptimizationIpMstVo;

@Component
@Transactional
public class OptMgmtAdapterService {

	@Lazy @Autowired
	private OptMgmtService optMgmtService;

	@Transactional(readOnly = true)
	public TbOptimizationIpMstListVo selectListPageOptimizeIpResult(TbOptimizationIpMstVo tbOptimizationIpMstVo) {
		return optMgmtService.selectListPageOptimizeIpResult(tbOptimizationIpMstVo);
	}
	
}
