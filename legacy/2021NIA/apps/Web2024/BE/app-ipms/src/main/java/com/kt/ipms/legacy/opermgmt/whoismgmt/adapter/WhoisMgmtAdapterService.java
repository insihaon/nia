package com.kt.ipms.legacy.opermgmt.whoismgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.whoismgmt.service.WhoisMgmtService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

@Component
@Transactional
public class WhoisMgmtAdapterService {

	@Lazy @Autowired
	private WhoisMgmtService whoisMgmtService;

	@Transactional(readOnly = true)
	public TbWhoisComplexListVo selectListStandByWhois(TbWhoisComplexVo tbWhoisComplexVo) {
		return whoisMgmtService.selectListBatchWhoisComplex(tbWhoisComplexVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhois(TbWhoisVo tbWhoisVo) {
		return whoisMgmtService.updateWhois(tbWhoisVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateResultCd(TbWhoisVo tbWhoisVo) {
		return whoisMgmtService.updateResultCd(tbWhoisVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisResultList() {
		return whoisMgmtService.updateWhoisResultList();
	}
}
